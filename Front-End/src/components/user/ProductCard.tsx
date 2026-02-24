import { useState } from "react";
import { CustomBadge } from "@/components/ui/CustomBadge";
import { Button } from "@/components/ui/button";
import { Heart, Star, Loader2 } from "lucide-react";
import { useNavigate } from "react-router";
import { PUBLIC_PATH } from "@/constants/path";
import type { ProductSearchResponse } from "@/types/product.type";
import { useWishlist } from "@/hooks/useWishlist";
import { useUser } from "@/context/UserContext";
import LoginModal from "@/components/user/LoginModal";


interface ProductCardProps {
  product: ProductSearchResponse;
}

export default function ProductCard({ product }: ProductCardProps) {
  const navigate = useNavigate();
  const { isAuthenticated } = useUser();
  const { isInWishlist, toggleWishlist, isAdding, isRemoving } = useWishlist();
  const [showLoginModal, setShowLoginModal] = useState(false);


  const displayPrice = product.displayPrice ?? 0;
  const oldPrice = product.originalPrice ?? 0;

  const discountPercent = product.discountPercent ?? 0;


  const productId = product.id || 0;
  const inWishlist = productId > 0 ? isInWishlist(productId) : false;
  const isLoading = isAdding || isRemoving;


  const handleProductClick = () => {
    navigate(`${PUBLIC_PATH.HOME}product/${product.slug}`);
  };

  const handleWishlistClick = (e: React.MouseEvent) => {
    e.stopPropagation(); // Ngăn chặn sự kiện click lan ra Card

    if (!isAuthenticated) {
      setShowLoginModal(true);
      return;
    }

    if (productId > 0) {
      toggleWishlist(productId);
    }
  };

  // Format giá tiền
  const formatPrice = (price: number) => {
    return new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(price);
  };

  return (
    <div
      className="group relative overflow-hidden bg-white rounded-sm border border-gray-200 hover:border-blue-400 transition-all duration-200 cursor-pointer"
      onClick={handleProductClick}
    >
      {/* Product Image */}
      <div className="relative aspect-[5/4] overflow-hidden bg-gray-50">
        <img
          src={product.thumbnail}
          alt={product.name}
          className="w-full h-full object-cover group-hover:scale-102 transition-transform duration-200 p-2"
        />

        {/* Badges overlay */}
        {discountPercent > 0 && (
          <CustomBadge variant="error" size="sm" className="absolute top-2 left-2 text-red-500 font-semibold">
            -{discountPercent}%
          </CustomBadge>
        )}

        {/* Wishlist Button */}
        <Button
          variant="ghost"
          size="icon"
          onClick={handleWishlistClick}
          disabled={isLoading || productId === 0}
          className={`absolute top-2 right-2 w-9 h-9 rounded-full bg-white/90 backdrop-blur-sm shadow-sm hover:bg-white transition-all ${inWishlist ? "text-red-500" : "text-gray-400 hover:text-red-500"
            }`}
          title={inWishlist ? "Xóa khỏi yêu thích" : "Thêm vào yêu thích"}
        >
          {isLoading ? (
            <Loader2 className="w-5 h-5 animate-spin" />
          ) : (
            <Heart className={`w-5 h-5 ${inWishlist ? "fill-current" : ""}`} />
          )}
        </Button>
      </div>

      <div className="p-4 space-y-2.5">
        {/* Product Name */}
        <h3 className="font-semibold text-lg text-gray-800 line-clamp-2 leading-snug min-h-[3.25rem]">
          {product.name}
        </h3>


        <div className="space-y-1.5">
          <div className="flex items-baseline gap-2 flex-wrap">
            <span className="text-2xl font-bold text-red-500">
              {formatPrice(displayPrice)}
            </span>
            {discountPercent > 0 && oldPrice > 0 && (
              <span className="text-base text-gray-400 line-through">
                {formatPrice(oldPrice)}
              </span>
            )}
          </div>

          {/* Smember & Installment */}
          <div className="flex flex-wrap gap-2">
            <span className="inline-flex items-center text-sm text-blue-600 bg-blue-50 px-2 py-1 rounded font-medium">
              Smember -{formatPrice(displayPrice * 0.01)}
            </span>
            <span className="inline-flex items-center text-sm text-emerald-600 bg-emerald-50 px-2 py-1 rounded font-medium">
              Trả góp 0%
            </span>

          </div>
          <div className="flex items-center gap-1.5 pt-1">
            <Star className="w-4 h-4 fill-amber-400 text-amber-400" />
            <span className="text-base font-medium text-gray-600">
              {product.rating || 0}
            </span>
          </div>


        </div>

      </div>

      {/* Login Modal */}
      <LoginModal open={showLoginModal} onOpenChange={setShowLoginModal} />
    </div>
  );
}
