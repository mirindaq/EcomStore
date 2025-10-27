import { useState, useEffect } from 'react'
import { useNavigate, useLocation } from 'react-router'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { RadioGroup, RadioGroupItem } from '@/components/ui/radio-group'
import { Badge } from '@/components/ui/badge'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog'
import { 
  ShoppingCart, 
  MapPin, 
  CreditCard, 
  Truck,
  CheckCircle,
  ArrowLeft,
  Wallet,
  Banknote,
  Store,
  Tag,
  X,
  Percent,
  Gift
} from 'lucide-react'
import { toast } from 'sonner'
import { voucherService } from '@/services/voucher.service'
import type { VoucherAvailableResponse } from '@/types/voucher.type'
import { useQuery } from '@/hooks/useQuery'

type CheckoutItem = {
  productVariantId: number
  productName: string
  productImage: string
  sku: string
  quantity: number
  price: number
  discount: number
}

export default function Checkout() {
  const navigate = useNavigate()
  const location = useLocation()
  const [items, setItems] = useState<CheckoutItem[]>([])
  const [deliveryType, setDeliveryType] = useState('delivery') // 'delivery' hoặc 'pickup'
  const [paymentMethod, setPaymentMethod] = useState('cod')
  
  // Voucher states
  const [voucherCode, setVoucherCode] = useState('')
  const [appliedVoucher, setAppliedVoucher] = useState<VoucherAvailableResponse | null>(null)
  const [isVoucherDialogOpen, setIsVoucherDialogOpen] = useState(false)
  
  // Form data
  const [formData, setFormData] = useState({
    fullName: '',
    phone: '',
    email: '',
    address: '',
    city: '',
    district: '',
    ward: '',
    note: ''
  })

  useEffect(() => {
    // Lấy dữ liệu từ location state
    if (location.state?.selectedItems) {
      setItems(location.state.selectedItems)
    } else {
      // Nếu không có dữ liệu, redirect về cart
      toast.error('Vui lòng chọn sản phẩm để thanh toán')
      navigate('/cart')
    }
  }, [location.state, navigate])

  const { data: vouchersData, isLoading: loadingVouchers } = useQuery<{ data: VoucherAvailableResponse[] }>(
    () => voucherService.getAvailableVouchers(),
    {
      queryKey: ['availableVouchers'],
      enabled: items.length > 0,
      onError: () => {
        console.error('Lỗi khi tải voucher')
      }
    }
  )

  const availableVouchers = vouchersData?.data || []

  const applyVoucherByCode = () => {
    if (!voucherCode.trim()) {
      toast.error('Vui lòng nhập mã voucher')
      return
    }

    const voucher = availableVouchers.find(v => v.code.toUpperCase() === voucherCode.toUpperCase())
    
    if (!voucher) {
      toast.error('Mã voucher không hợp lệ hoặc đã hết hạn')
      return
    }

    applyVoucher(voucher)
  }

  const applyVoucher = (voucher: VoucherAvailableResponse) => {
    const subtotal = calculateSubtotal()
    
    if (subtotal < voucher.minOrderAmount) {
      toast.error(`Đơn hàng tối thiểu ${formatPrice(voucher.minOrderAmount)} để áp dụng voucher này`)
      return
    }

    setAppliedVoucher(voucher)
    setVoucherCode(voucher.code)
    setIsVoucherDialogOpen(false)
    toast.success('Áp dụng voucher thành công!')
  }

  const removeVoucher = () => {
    setAppliedVoucher(null)
    setVoucherCode('')
    toast.success('Đã hủy áp dụng voucher')
  }

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target
    setFormData(prev => ({
      ...prev,
      [name]: value
    }))
  }

  const calculateSubtotal = () => {
    return items.reduce((total, item) => {
      const discountedPrice = item.price * (1 - item.discount / 100)
      return total + (discountedPrice * item.quantity)
    }, 0)
  }

  const calculateShippingFee = () => {
    // Nếu nhận tại cửa hàng thì không có phí ship
    if (deliveryType === 'pickup') {
      return 0
    }
    
    // Phí ship cố định hoặc miễn phí nếu đơn hàng > 500k
    const subtotal = calculateSubtotal()
    return subtotal > 500000 ? 0 : 30000
  }

  const calculateVoucherDiscount = () => {
    if (!appliedVoucher) return 0
    
    const subtotal = calculateSubtotal()
    let discount = 0
    
    // Tính giảm giá theo % hoặc cố định
    discount = (subtotal * appliedVoucher.discount) / 100
    
    // Giới hạn số tiền giảm tối đa
    if (appliedVoucher.maxDiscountAmount > 0 && discount > appliedVoucher.maxDiscountAmount) {
      discount = appliedVoucher.maxDiscountAmount
    }
    
    return discount
  }

  const calculateTotal = () => {
    return calculateSubtotal() + calculateShippingFee() - calculateVoucherDiscount()
  }

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND'
    }).format(price)
  }

  const handleSubmitOrder = () => {
    // Validate form - tên và số điện thoại bắt buộc cho cả 2 trường hợp
    if (!formData.fullName || !formData.phone) {
      toast.error('Vui lòng điền đầy đủ họ tên và số điện thoại')
      return
    }

    // Nếu giao hàng tận nơi thì validate địa chỉ
    if (deliveryType === 'delivery' && (!formData.address || !formData.city)) {
      toast.error('Vui lòng điền đầy đủ thông tin giao hàng')
      return
    }

    // Phone validation
    const phoneRegex = /(84|0[3|5|7|8|9])+([0-9]{8})\b/
    if (!phoneRegex.test(formData.phone)) {
      toast.error('Số điện thoại không hợp lệ')
      return
    }

    // TODO: Call API to create order
    toast.success('Đặt hàng thành công!')
    console.log('Order data:', {
      items,
      formData,
      deliveryType,
      paymentMethod,
      voucher: appliedVoucher ? {
        code: appliedVoucher.code,
        discount: calculateVoucherDiscount()
      } : null,
      subtotal: calculateSubtotal(),
      shippingFee: calculateShippingFee(),
      voucherDiscount: calculateVoucherDiscount(),
      total: calculateTotal()
    })
    
    // Redirect to success page or order history
    // navigate('/account/orders')
  }

  if (items.length === 0) {
    return null
  }

  return (
    <div className="container mx-auto px-4 py-8 max-w-7xl">
      {/* Header */}
      <div className="mb-8">
        <Button
          variant="ghost"
          onClick={() => navigate('/cart')}
          className="mb-4 hover:bg-gray-100"
        >
          <ArrowLeft className="w-4 h-4 mr-2" />
          Quay lại giỏ hàng
        </Button>
        <h1 className="text-3xl font-bold mb-2">Thanh toán</h1>
        <p className="text-gray-600">Hoàn tất đơn hàng của bạn</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Left Column - Forms */}
        <div className="lg:col-span-2 space-y-6">
          {/* Shipping Information */}
          <div className="bg-white rounded-lg shadow-sm border p-6">
            <div className="flex items-center space-x-2 mb-6">
              <MapPin className="w-5 h-5 text-primary" />
              <h2 className="text-xl font-semibold">Thông tin nhận hàng</h2>
            </div>

            <div className="space-y-4">
              {/* Delivery Type Selection */}
              <div className="bg-gray-50 rounded-lg">
                <Label className="text-base font-semibold mb-3 block">
                  Hình thức nhận hàng <span className="text-red-500">*</span>
                </Label>
                <RadioGroup value={deliveryType} onValueChange={setDeliveryType}>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    {/* Nhận tại cửa hàng */}
                    <div className={`flex items-start space-x-3 border-2 rounded-lg p-4 cursor-pointer transition-all ${
                      deliveryType === 'pickup' ? 'border-primary bg-primary/5' : 'border-gray-200 bg-white hover:border-gray-300'
                    }`}>
                      <RadioGroupItem value="pickup" id="pickup" />
                      <div className="flex-1">
                        <Label htmlFor="pickup" className="cursor-pointer">
                          <div className="flex items-center space-x-2">
                            <Store className="w-5 h-5 text-primary" />
                            <span className="font-medium text-base">Nhận tại cửa hàng</span>
                          </div>
                          <p className="text-sm text-gray-500 mt-1">
                            Đến cửa hàng để nhận hàng
                          </p>
                        </Label>
                      </div>
                    </div>

                    {/* Giao hàng tận nơi */}
                    <div className={`flex items-start space-x-3 border-2 rounded-lg p-4 cursor-pointer transition-all ${
                      deliveryType === 'delivery' ? 'border-primary bg-primary/5' : 'border-gray-200 bg-white hover:border-gray-300'
                    }`}>
                      <RadioGroupItem value="delivery" id="delivery" />
                      <div className="flex-1">
                        <Label htmlFor="delivery" className="cursor-pointer">
                          <div className="flex items-center space-x-2">
                            <Truck className="w-5 h-5 text-primary" />
                            <span className="font-medium text-base">Giao hàng tận nơi</span>
                          </div>
                          <p className="text-sm text-gray-500 mt-1">
                            Giao hàng đến địa chỉ của bạn
                          </p>
                        </Label>
                      </div>
                    </div>
                  </div>
                </RadioGroup>
              </div>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="fullName">
                    Họ và tên <span className="text-red-500">*</span>
                  </Label>
                  <Input
                    id="fullName"
                    name="fullName"
                    placeholder="Nguyễn Văn A"
                    value={formData.fullName}
                    onChange={handleInputChange}
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="phone">
                    Số điện thoại <span className="text-red-500">*</span>
                  </Label>
                  <Input
                    id="phone"
                    name="phone"
                    placeholder="0987654321"
                    value={formData.phone}
                    onChange={handleInputChange}
                  />
                </div>
              </div>

              <div className="space-y-2">
                <Label htmlFor="email">Email</Label>
                <Input
                  id="email"
                  name="email"
                  type="email"
                  placeholder="email@example.com"
                  value={formData.email}
                  onChange={handleInputChange}
                />
              </div>

              {/* Chỉ hiển thị form địa chỉ khi chọn giao hàng tận nơi */}
              {deliveryType === 'delivery' && (
                <>
                  <div className="space-y-2">
                    <Label htmlFor="address">
                      Địa chỉ <span className="text-red-500">*</span>
                    </Label>
                    <Input
                      id="address"
                      name="address"
                      placeholder="Số nhà, tên đường (Vui lòng chọn quận/huyện và phường/xã trước)"
                      value={formData.address}
                      onChange={handleInputChange}
                    />
                  </div>

                  <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <div className="space-y-2">
                      <Label htmlFor="city">
                        Tỉnh/Thành phố <span className="text-red-500">*</span>
                      </Label>
                      <Input
                        id="city"
                        name="city"
                        placeholder="Hồ Chí Minh"
                        value={formData.city}
                        onChange={handleInputChange}
                      />
                    </div>

                    <div className="space-y-2">
                      <Label htmlFor="district">Quận/Huyện</Label>
                      <Input
                        id="district"
                        name="district"
                        placeholder="Chọn quận/huyện"
                        value={formData.district}
                        onChange={handleInputChange}
                      />
                    </div>

                    <div className="space-y-2">
                      <Label htmlFor="ward">Phường/Xã</Label>
                      <Input
                        id="ward"
                        name="ward"
                        placeholder="Chọn phường/xã"
                        value={formData.ward}
                        onChange={handleInputChange}
                      />
                    </div>
                  </div>
                </>
              )}

              {/* Hiển thị địa chỉ cửa hàng khi chọn nhận tại cửa hàng */}
              {deliveryType === 'pickup' && (
                <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
                  <div className="flex items-start space-x-3">
                    <Store className="w-5 h-5 text-blue-600 mt-0.5" />
                    <div>
                      <h4 className="font-semibold text-blue-900 mb-2">Địa chỉ cửa hàng</h4>
                      <p className="text-sm text-blue-800">
                        <strong>Cửa hàng chính:</strong> 123 Đường ABC, Phường XYZ, Quận 1, TP.HCM
                      </p>
                      <p className="text-sm text-blue-800 mt-1">
                        <strong>Giờ làm việc:</strong> 8:00 - 22:00 hàng ngày
                      </p>
                      <p className="text-sm text-blue-800 mt-1">
                        <strong>Hotline:</strong> 1900 xxxx
                      </p>
                    </div>
                  </div>
                </div>
              )}

              <div className="space-y-2">
                <Label htmlFor="note">Ghi chú đơn hàng</Label>
                <textarea
                  id="note"
                  name="note"
                  rows={3}
                  placeholder="Ghi chú về đơn hàng, ví dụ: thời gian hay chỉ dẫn địa điểm giao hàng chi tiết hơn"
                  value={formData.note}
                  onChange={handleInputChange}
                  className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary"
                />
              </div>
            </div>
          </div>

          {/* Voucher Section */}
          <div className="bg-white rounded-lg shadow-sm border p-6">
            <div className="flex items-center space-x-2 mb-6">
              <Tag className="w-5 h-5 text-primary" />
              <h2 className="text-xl font-semibold">Mã giảm giá</h2>
            </div>

            <div className="space-y-4">
              {/* Voucher Input */}
              <div className="flex space-x-2">
                <Input
                  placeholder="Nhập mã voucher"
                  value={voucherCode}
                  onChange={(e) => setVoucherCode(e.target.value.toUpperCase())}
                  disabled={!!appliedVoucher}
                  className="flex-1"
                  onKeyPress={(e) => {
                    if (e.key === 'Enter') {
                      applyVoucherByCode()
                    }
                  }}
                />
                <Button
                  onClick={applyVoucherByCode}
                  disabled={!!appliedVoucher}
                  className="min-w-[100px]"
                >
                  Áp dụng
                </Button>
              </div>

              {/* Applied Voucher Display */}
              {appliedVoucher && (
                <div className="bg-green-50 border border-green-200 rounded-lg p-4 animate-in slide-in-from-top-2">
                  <div className="flex items-start justify-between">
                    <div className="flex items-start space-x-3">
                      <div className="bg-green-100 rounded-full p-2">
                        <Gift className="w-5 h-5 text-green-600" />
                      </div>
                      <div className="flex-1">
                        <div className="flex items-center space-x-2 mb-1">
                          <h4 className="font-semibold text-green-900">{appliedVoucher.name}</h4>
                          <Badge variant="default" className="bg-green-600">
                            -{appliedVoucher.discount}%
                          </Badge>
                        </div>
                        <p className="text-sm text-green-800 mb-1">{appliedVoucher.description}</p>
                        <p className="text-xs text-green-600">
                          Giảm {formatPrice(calculateVoucherDiscount())}
                        </p>
                      </div>
                    </div>
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={removeVoucher}
                      className="text-green-700 hover:text-green-900 hover:bg-green-100"
                    >
                      <X className="w-4 h-4" />
                    </Button>
                  </div>
                </div>
              )}

              {/* Select Voucher Button */}
              {!appliedVoucher && (
                <Dialog open={isVoucherDialogOpen} onOpenChange={setIsVoucherDialogOpen}>
                  <DialogTrigger asChild>
                    <Button variant="outline" className="w-full">
                      <Percent className="w-4 h-4 mr-2" />
                      Chọn voucher có sẵn ({availableVouchers.length})
                    </Button>
                  </DialogTrigger>
                  <DialogContent className="max-w-2xl max-h-[80vh] overflow-y-auto">
                    <DialogHeader>
                      <DialogTitle>Chọn voucher</DialogTitle>
                      <DialogDescription>
                        Chọn voucher bạn muốn áp dụng cho đơn hàng
                      </DialogDescription>
                    </DialogHeader>

                    <div className="space-y-3 mt-4">
                      {loadingVouchers ? (
                        <div className="text-center py-8 text-gray-500">
                          Đang tải voucher...
                        </div>
                      ) : availableVouchers.length === 0 ? (
                        <div className="text-center py-8 text-gray-500">
                          Không có voucher khả dụng
                        </div>
                      ) : (
                        availableVouchers.map((voucher) => {
                          const subtotal = calculateSubtotal()
                          const isEligible = subtotal >= voucher.minOrderAmount
                          
                          return (
                            <div
                              key={voucher.id}
                              className={`border rounded-lg p-4 transition-all cursor-pointer ${
                                isEligible 
                                  ? 'hover:border-primary hover:bg-primary/5' 
                                  : 'opacity-60 cursor-not-allowed'
                              }`}
                              onClick={() => isEligible && applyVoucher(voucher)}
                            >
                              <div className="flex items-start space-x-3">
                                <div className={`rounded-full p-2 ${
                                  isEligible ? 'bg-primary/10' : 'bg-gray-100'
                                }`}>
                                  <Percent className={`w-5 h-5 ${
                                    isEligible ? 'text-primary' : 'text-gray-400'
                                  }`} />
                                </div>
                                <div className="flex-1">
                                  <div className="flex items-center space-x-2 mb-1">
                                    <h4 className="font-semibold">{voucher.name}</h4>
                                    <Badge variant="secondary">
                                      -{voucher.discount}%
                                    </Badge>
                                  </div>
                                  <p className="text-sm text-gray-600 mb-2">
                                    {voucher.description}
                                  </p>
                                  <div className="flex items-center space-x-4 text-xs text-gray-500">
                                    <span>Mã: {voucher.code}</span>
                                    <span>Đơn tối thiểu: {formatPrice(voucher.minOrderAmount)}</span>
                                    {voucher.maxDiscountAmount > 0 && (
                                      <span>Giảm tối đa: {formatPrice(voucher.maxDiscountAmount)}</span>
                                    )}
                                  </div>
                                  {!isEligible && (
                                    <p className="text-xs text-red-600 mt-2">
                                      Đơn hàng cần thêm {formatPrice(voucher.minOrderAmount - subtotal)} để áp dụng
                                    </p>
                                  )}
                                </div>
                              </div>
                            </div>
                          )
                        })
                      )}
                    </div>
                  </DialogContent>
                </Dialog>
              )}
            </div>
          </div>

          {/* Payment Method */}
          <div className="bg-white rounded-lg shadow-sm border p-6">
            <div className="flex items-center space-x-2 mb-6">
              <CreditCard className="w-5 h-5 text-primary" />
              <h2 className="text-xl font-semibold">Phương thức thanh toán</h2>
            </div>

            <RadioGroup value={paymentMethod} onValueChange={setPaymentMethod}>
              <div className="space-y-4">
                {/* COD */}
                <div className="flex items-start space-x-3 border rounded-lg p-4 hover:bg-gray-50 transition-colors cursor-pointer">
                  <RadioGroupItem value="cod" id="cod" />
                  <div className="flex-1">
                    <Label htmlFor="cod" className="cursor-pointer">
                      <div className="flex items-center space-x-2">
                        <Truck className="w-5 h-5 text-primary" />
                        <span className="font-medium">Thanh toán khi nhận hàng (COD)</span>
                      </div>
                      <p className="text-sm text-gray-500 mt-1">
                        Thanh toán bằng tiền mặt khi nhận hàng
                      </p>
                    </Label>
                  </div>
                </div>

                {/* Banking */}
                <div className="flex items-start space-x-3 border rounded-lg p-4 hover:bg-gray-50 transition-colors cursor-pointer">
                  <RadioGroupItem value="banking" id="banking" />
                  <div className="flex-1">
                    <Label htmlFor="banking" className="cursor-pointer">
                      <div className="flex items-center space-x-2">
                        <Banknote className="w-5 h-5 text-primary" />
                        <span className="font-medium">Chuyển khoản ngân hàng</span>
                      </div>
                      <p className="text-sm text-gray-500 mt-1">
                        Chuyển khoản trực tiếp qua ngân hàng
                      </p>
                    </Label>
                  </div>
                </div>

                {/* E-wallet */}
                <div className="flex items-start space-x-3 border rounded-lg p-4 hover:bg-gray-50 transition-colors cursor-pointer opacity-50">
                  <RadioGroupItem value="wallet" id="wallet" disabled />
                  <div className="flex-1">
                    <Label htmlFor="wallet" className="cursor-pointer">
                      <div className="flex items-center space-x-2">
                        <Wallet className="w-5 h-5 text-primary" />
                        <span className="font-medium">Ví điện tử</span>
                        <Badge variant="secondary" className="text-xs">Sắp có</Badge>
                      </div>
                      <p className="text-sm text-gray-500 mt-1">
                        Thanh toán qua Momo, ZaloPay, VNPay
                      </p>
                    </Label>
                  </div>
                </div>
              </div>
            </RadioGroup>
          </div>
        </div>

        {/* Right Column - Order Summary */}
        <div className="lg:col-span-1">
          <div className="bg-white rounded-lg shadow-sm border p-6 sticky top-4">
            <div className="flex items-center space-x-2 mb-6">
              <ShoppingCart className="w-5 h-5 text-primary" />
              <h2 className="text-xl font-semibold">Đơn hàng của bạn</h2>
            </div>

            {/* Order Items */}
            <div className="space-y-4 mb-6 max-h-[400px] overflow-y-auto">
              {items.map((item) => (
                <div key={item.productVariantId} className="flex space-x-3 pb-4 border-b last:border-b-0">
                  <div className="relative">
                    <img
                      src={item.productImage}
                      alt={item.productName}
                      className="w-16 h-16 object-cover rounded-lg"
                    />
                    <Badge 
                      className="absolute -top-2 -right-2 w-6 h-6 flex items-center justify-center p-0 text-xs"
                      variant="default"
                    >
                      {item.quantity}
                    </Badge>
                  </div>
                  
                  <div className="flex-1 min-w-0">
                    <h4 className="font-medium text-sm line-clamp-2 mb-1">
                      {item.productName}
                    </h4>
                    <p className="text-xs text-gray-500 mb-2">SKU: {item.sku}</p>
                    
                    <div className="flex items-center justify-between">
                      {item.discount > 0 ? (
                        <div className="flex items-center space-x-2">
                          <span className="text-sm font-semibold text-red-600">
                            {formatPrice(item.price * (1 - item.discount / 100))}
                          </span>
                          <span className="text-xs text-gray-400 line-through">
                            {formatPrice(item.price)}
                          </span>
                        </div>
                      ) : (
                        <span className="text-sm font-semibold">
                          {formatPrice(item.price)}
                        </span>
                      )}
                    </div>
                  </div>
                </div>
              ))}
            </div>

            {/* Order Summary */}
            <div className="space-y-3 pt-4 border-t">
              <div className="flex justify-between text-gray-600">
                <span>Tạm tính:</span>
                <span className="font-medium">{formatPrice(calculateSubtotal())}</span>
              </div>
              
              <div className="flex justify-between text-gray-600">
                <span>Phí vận chuyển:</span>
                <span className="font-medium">
                  {calculateShippingFee() === 0 ? (
                    <span className="text-green-600">Miễn phí</span>
                  ) : (
                    formatPrice(calculateShippingFee())
                  )}
                </span>
              </div>

              {deliveryType === 'pickup' && (
                <div className="flex items-center space-x-2 text-xs text-green-600 bg-green-50 p-2 rounded">
                  <CheckCircle className="w-4 h-4" />
                  <span>Miễn phí vận chuyển - Nhận tại cửa hàng</span>
                </div>
              )}

              {deliveryType === 'delivery' && calculateShippingFee() === 0 && (
                <div className="flex items-center space-x-2 text-xs text-green-600 bg-green-50 p-2 rounded">
                  <CheckCircle className="w-4 h-4" />
                  <span>Miễn phí vận chuyển cho đơn hàng trên 500.000đ</span>
                </div>
              )}

              {appliedVoucher && (
                <div className="flex justify-between text-green-600">
                  <span className="flex items-center space-x-1">
                    <Tag className="w-4 h-4" />
                    <span>Giảm giá voucher:</span>
                  </span>
                  <span className="font-medium">-{formatPrice(calculateVoucherDiscount())}</span>
                </div>
              )}

              <div className="flex justify-between text-lg font-bold pt-3 border-t">
                <span>Tổng cộng:</span>
                <span className="text-red-600">{formatPrice(calculateTotal())}</span>
              </div>
            </div>

            {/* Place Order Button */}
            <Button 
              className="w-full mt-6 h-12 text-base"
              onClick={handleSubmitOrder}
            >
              <CheckCircle className="w-5 h-5 mr-2" />
              Đặt hàng
            </Button>

            {/* Security Note */}
            <p className="text-xs text-gray-500 text-center mt-4">
              Thông tin của bạn được bảo mật và mã hóa
            </p>
          </div>
        </div>
      </div>
    </div>
  )
}

