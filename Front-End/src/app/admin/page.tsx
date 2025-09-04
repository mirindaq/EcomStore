"use client"

import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { 
  Package,
  Users,
  ShoppingCart,
  DollarSign,
  TrendingUp,
  TrendingDown,
  Eye
} from "lucide-react"

const stats = [
  {
    title: "Tổng doanh thu",
    value: "₫45,231,000",
    change: "+20.1%",
    trend: "up",
    icon: DollarSign,
    description: "So với tháng trước"
  },
  {
    title: "Đơn hàng",
    value: "1,234",
    change: "+12.5%",
    trend: "up", 
    icon: ShoppingCart,
    description: "Đơn hàng mới"
  },
  {
    title: "Sản phẩm",
    value: "567",
    change: "+5.2%",
    trend: "up",
    icon: Package,
    description: "Sản phẩm đang bán"
  },
  {
    title: "Khách hàng",
    value: "890",
    change: "-2.1%",
    trend: "down",
    icon: Users,
    description: "Khách hàng hoạt động"
  },
]

const recentOrders = [
  {
    id: "ORD-001",
    customer: "Nguyễn Văn A",
    product: "iPhone 15 Pro",
    amount: "₫25,990,000",
    status: "completed",
    date: "2024-01-15"
  },
  {
    id: "ORD-002", 
    customer: "Trần Thị B",
    product: "MacBook Air M2",
    amount: "₫28,990,000",
    status: "processing",
    date: "2024-01-15"
  },
  {
    id: "ORD-003",
    customer: "Lê Văn C", 
    product: "Samsung Galaxy S24",
    amount: "₫20,990,000",
    status: "pending",
    date: "2024-01-14"
  },
  {
    id: "ORD-004",
    customer: "Phạm Thị D",
    product: "iPad Pro",
    amount: "₫22,990,000", 
    status: "completed",
    date: "2024-01-14"
  },
]

const getStatusBadge = (status: string) => {
  switch (status) {
    case "completed":
      return <Badge variant="default" className="bg-green-100 text-green-800">Hoàn thành</Badge>
    case "processing":
      return <Badge variant="default" className="bg-blue-100 text-blue-800">Đang xử lý</Badge>
    case "pending":
      return <Badge variant="default" className="bg-yellow-100 text-yellow-800">Chờ xử lý</Badge>
    default:
      return <Badge variant="secondary">{status}</Badge>
  }
}

export default function AdminDashboard() {
  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Dashboard</h1>
          <p className="text-muted-foreground">
            Chào mừng trở lại! Đây là tổng quan về cửa hàng của bạn.
          </p>
        </div>
        <Button>
          <Eye className="mr-2 h-4 w-4" />
          Xem báo cáo
        </Button>
      </div>

      {/* Stats Cards */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        {stats.map((stat) => (
          <Card key={stat.title}>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {stat.title}
              </CardTitle>
              <stat.icon className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{stat.value}</div>
              <div className="flex items-center space-x-2 text-xs text-muted-foreground">
                {stat.trend === "up" ? (
                  <TrendingUp className="h-3 w-3 text-green-500" />
                ) : (
                  <TrendingDown className="h-3 w-3 text-red-500" />
                )}
                <span className={stat.trend === "up" ? "text-green-500" : "text-red-500"}>
                  {stat.change}
                </span>
                <span>{stat.description}</span>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Recent Orders */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-7">
        <Card className="col-span-4">
          <CardHeader>
            <CardTitle>Đơn hàng gần đây</CardTitle>
            <CardDescription>
              Danh sách các đơn hàng mới nhất
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {recentOrders.map((order) => (
                <div key={order.id} className="flex items-center justify-between p-4 border rounded-lg">
                  <div className="space-y-1">
                    <div className="flex items-center gap-2">
                      <p className="text-sm font-medium">{order.id}</p>
                      {getStatusBadge(order.status)}
                    </div>
                    <p className="text-sm text-muted-foreground">{order.customer}</p>
                    <p className="text-xs text-muted-foreground">{order.product}</p>
                  </div>
                  <div className="text-right">
                    <p className="text-sm font-medium">{order.amount}</p>
                    <p className="text-xs text-muted-foreground">{order.date}</p>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        <Card className="col-span-3">
          <CardHeader>
            <CardTitle>Thống kê nhanh</CardTitle>
            <CardDescription>
              Các chỉ số quan trọng
            </CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="flex items-center justify-between">
              <span className="text-sm">Tỷ lệ chuyển đổi</span>
              <span className="text-sm font-medium">3.2%</span>
            </div>
            <div className="flex items-center justify-between">
              <span className="text-sm">Giá trị đơn hàng trung bình</span>
              <span className="text-sm font-medium">₫1,250,000</span>
            </div>
            <div className="flex items-center justify-between">
              <span className="text-sm">Khách hàng mới</span>
              <span className="text-sm font-medium">45</span>
            </div>
            <div className="flex items-center justify-between">
              <span className="text-sm">Sản phẩm bán chạy</span>
              <span className="text-sm font-medium">iPhone 15</span>
            </div>
            <div className="flex items-center justify-between">
              <span className="text-sm">Tồn kho thấp</span>
              <span className="text-sm font-medium text-red-500">12 sản phẩm</span>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
