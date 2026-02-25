package iuh.fit.ecommerce.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // Common errors (1000-1999)
    INTERNAL_SERVER_ERROR(1000, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error occurred. Please try again later or contact support if the problem persists."),
    INVALID_PARAMETER(1001, HttpStatus.BAD_REQUEST, "Invalid parameter provided. Please check your input and try again."),
    VALIDATION_ERROR(1002, HttpStatus.BAD_REQUEST, "Validation failed. Please check the provided data and ensure all required fields are correctly filled."),
    UNAUTHORIZED(1003, HttpStatus.UNAUTHORIZED, "Unauthorized access. Please authenticate and try again."),
    FORBIDDEN(1004, HttpStatus.FORBIDDEN, "Access denied. You don't have the required permissions to perform this action."),
    RESOURCE_NOT_FOUND(1005, HttpStatus.NOT_FOUND, "The requested resource was not found. Please verify the resource identifier and try again."),
    CONFLICT(1006, HttpStatus.CONFLICT, "A conflict occurred with the current state of the resource. Please refresh and try again."),
    ILLEGAL_STATE(1007, HttpStatus.BAD_REQUEST, "Invalid operation. The current state does not allow this action."),
    ILLEGAL_ARGUMENT(1008, HttpStatus.BAD_REQUEST, "Invalid argument provided. Please check your input parameters."),

    // Authentication & Authorization errors (2000-2099)
    AUTHENTICATION_FAILED(2000, HttpStatus.UNAUTHORIZED, "Authentication failed. Invalid credentials provided."),
    TOKEN_INVALID(2001, HttpStatus.UNAUTHORIZED, "Invalid or expired token. Please login again to obtain a new token."),
    TOKEN_MISSING(2002, HttpStatus.UNAUTHORIZED, "Authentication token is missing. Please provide a valid token in the request header."),
    ACCESS_DENIED(2003, HttpStatus.FORBIDDEN, "Access denied. You don't have the required role or permissions."),

    // Customer errors (3000-3099)
    CUSTOMER_NOT_FOUND(3000, HttpStatus.NOT_FOUND, "Customer not found. The specified customer ID does not exist in the system."),
    CUSTOMER_ALREADY_EXISTS(3001, HttpStatus.CONFLICT, "Customer already exists. A customer with the provided information already exists in the system."),
    CUSTOMER_EMAIL_EXISTS(3002, HttpStatus.CONFLICT, "Email already registered. This email address is already associated with another customer account."),
    CUSTOMER_PHONE_EXISTS(3003, HttpStatus.CONFLICT, "Phone number already registered. This phone number is already associated with another customer account."),

    // Address errors (3100-3199)
    ADDRESS_NOT_FOUND(3100, HttpStatus.NOT_FOUND, "Address not found. The specified address ID does not exist in the system."),
    ADDRESS_NOT_BELONGS_TO_CUSTOMER(3101, HttpStatus.NOT_FOUND, "Address does not belong to this customer. You can only access addresses that belong to your account."),
    ADDRESS_CANNOT_REMOVE_DEFAULT(3102, HttpStatus.BAD_REQUEST, "Cannot remove default status from the only address. You must have at least one default address. Please set another address as default first."),
    WARD_NOT_FOUND(3103, HttpStatus.NOT_FOUND, "Ward not found. The specified ward ID does not exist in the system."),

    // Product errors (3200-3299)
    PRODUCT_NOT_FOUND(3200, HttpStatus.NOT_FOUND, "Product not found. The specified product ID does not exist in the system."),
    PRODUCT_SLUG_NOT_FOUND(3201, HttpStatus.NOT_FOUND, "Product not found. The specified product slug does not exist in the system."),
    PRODUCT_NAME_EXISTS(3202, HttpStatus.CONFLICT, "Product name already exists. A product with this name is already registered in the system."),
    PRODUCT_VARIANT_NOT_FOUND(3203, HttpStatus.NOT_FOUND, "Product variant not found. The specified product variant ID does not exist in the system."),
    PRODUCT_VARIANT_IDS_EMPTY(3204, HttpStatus.BAD_REQUEST, "Product variant IDs list is empty. Please provide at least one product variant ID."),

    // Cart errors (3250-3259)
    CART_NOT_FOUND(3250, HttpStatus.NOT_FOUND, "Cart not found. The specified cart does not exist in the system."),
    CART_ITEM_NOT_FOUND(3251, HttpStatus.NOT_FOUND, "Cart item not found. The specified product variant is not in the cart."),

    // Order errors (3300-3399)
    ORDER_NOT_FOUND(3300, HttpStatus.NOT_FOUND, "Order not found. The specified order ID does not exist in the system."),
    ORDER_CART_EMPTY(3301, HttpStatus.BAD_REQUEST, "Cart is empty. Please add items to your cart before creating an order."),
    ORDER_NO_VALID_ITEMS(3302, HttpStatus.BAD_REQUEST, "No valid cart items found to create order. Please ensure your cart contains valid items with sufficient stock."),
    ORDER_INVALID_STATUS(3303, HttpStatus.BAD_REQUEST, "Invalid order status. The requested status transition is not allowed for this order."),
    ORDER_INVALID_PAYMENT_METHOD(3304, HttpStatus.BAD_REQUEST, "Unsupported payment method. The specified payment method is not available for this order."),
    ORDER_MINIMUM_AMOUNT_NOT_MET(3305, HttpStatus.BAD_REQUEST, "Order does not meet minimum amount requirement. Please add more items to meet the minimum order value."),

    // Voucher errors (3400-3499)
    VOUCHER_NOT_FOUND(3400, HttpStatus.NOT_FOUND, "Voucher not found. The specified voucher ID does not exist in the system."),
    VOUCHER_NOT_ASSIGNED(3401, HttpStatus.BAD_REQUEST, "Voucher not assigned to this customer. This voucher is not available for your account."),
    VOUCHER_ALREADY_USED(3402, HttpStatus.BAD_REQUEST, "Voucher already used by this customer. This voucher has already been redeemed."),
    VOUCHER_EXPIRED(3403, HttpStatus.BAD_REQUEST, "Voucher expired or not active. This voucher is no longer valid for use."),
    VOUCHER_MINIMUM_AMOUNT_NOT_MET(3404, HttpStatus.BAD_REQUEST, "Order does not meet minimum amount for voucher. Please ensure your order total meets the voucher's minimum requirement."),

    // Category errors (3500-3599)
    CATEGORY_NOT_FOUND(3500, HttpStatus.NOT_FOUND, "Category not found. The specified category ID does not exist in the system."),
    CATEGORY_NAME_EXISTS(3501, HttpStatus.CONFLICT, "Category name already exists. A category with this name is already registered in the system."),

    // Brand errors (3600-3699)
    BRAND_NOT_FOUND(3600, HttpStatus.NOT_FOUND, "Brand not found. The specified brand ID does not exist in the system."),
    BRAND_NAME_EXISTS(3601, HttpStatus.CONFLICT, "Brand name already exists. A brand with this name is already registered in the system."),

    // Variant errors (3700-3799)
    VARIANT_NOT_FOUND(3700, HttpStatus.NOT_FOUND, "Variant not found. The specified variant ID does not exist in the system."),
    VARIANT_VALUE_NOT_FOUND(3701, HttpStatus.NOT_FOUND, "Variant value not found. The specified variant value ID does not exist in the system."),
    VARIANT_VALUE_DUPLICATE(3702, HttpStatus.CONFLICT, "Duplicate variant value found. This value already exists for the specified variant."),
    VARIANT_VALUE_EXISTS(3703, HttpStatus.CONFLICT, "Variant value already exists. This combination of variant and value is already registered."),

    // Delivery errors (3800-3899)
    DELIVERY_NOT_FOUND(3800, HttpStatus.NOT_FOUND, "Delivery assignment not found. The specified delivery ID does not exist in the system."),
    DELIVERY_ONLY_TEAM_LEADER(3801, HttpStatus.BAD_REQUEST, "Only team leaders can assign shippers to orders. You don't have the required permissions."),
    DELIVERY_INVALID_STATUS(3802, HttpStatus.BAD_REQUEST, "Order must be in SHIPPED status to assign shipper. The current order status does not allow this action."),
    DELIVERY_SHIPPER_BUSY(3803, HttpStatus.CONFLICT, "Shipper is currently delivering another order. Please assign a different shipper or wait for the current delivery to complete."),
    DELIVERY_ALREADY_ASSIGNED(3804, HttpStatus.CONFLICT, "Order already has a shipper assigned. This order is already being handled by another shipper."),
    DELIVERY_NOT_ASSIGNED_TO_USER(3805, HttpStatus.FORBIDDEN, "You are not assigned to this delivery. You can only manage deliveries assigned to you."),
    DELIVERY_INVALID_STATUS_TO_START(3806, HttpStatus.BAD_REQUEST, "Delivery must be in ASSIGNED status to start. The current status does not allow starting the delivery."),
    DELIVERY_ANOTHER_ORDER_IN_PROGRESS(3807, HttpStatus.BAD_REQUEST, "You are currently delivering another order. Please complete the current delivery before starting a new one."),
    DELIVERY_INVALID_STATUS_TO_COMPLETE(3808, HttpStatus.BAD_REQUEST, "Delivery must be in DELIVERING status to complete. The current status does not allow completing the delivery."),

    // Chat errors (3900-3999)
    CHAT_NOT_FOUND(3900, HttpStatus.NOT_FOUND, "Chat not found. The specified chat ID does not exist in the system."),
    CHAT_ALREADY_EXISTS(3901, HttpStatus.CONFLICT, "Chat already exists for this customer. A chat session is already active for this customer."),
    CHAT_NOT_FOUND_FOR_CUSTOMER(3902, HttpStatus.NOT_FOUND, "Chat not found for customer. No chat session exists for the specified customer ID."),
    CHAT_CANNOT_ASSIGN_TO_SHIPPER(3903, HttpStatus.BAD_REQUEST, "Cannot assign chat to shipper. Only staff members can be assigned to handle chat sessions."),
    CHAT_CANNOT_TRANSFER_TO_SHIPPER(3904, HttpStatus.BAD_REQUEST, "Cannot transfer chats to shipper. Only staff members can be assigned to handle chat sessions."),
    CHAT_USER_NOT_CUSTOMER(3905, HttpStatus.BAD_REQUEST, "User is not the customer of this chat. You can only access chats that belong to you."),
    CHAT_NO_MESSAGES(3906, HttpStatus.NOT_FOUND, "No messages found in chat. The specified chat does not contain any messages."),
    CHAT_NO_STAFF_ASSIGNED(3907, HttpStatus.BAD_REQUEST, "Chat has no staff assigned. Please assign a staff member to handle this chat session."),
    CHAT_STAFF_NOT_ASSIGNED(3908, HttpStatus.BAD_REQUEST, "Staff is not assigned to this chat. You don't have permission to manage this chat session."),

    // Promotion errors (4000-4099)
    PROMOTION_NOT_FOUND(4000, HttpStatus.NOT_FOUND, "Promotion not found. The specified promotion ID does not exist in the system."),

    // Feedback errors (4050-4059)
    FEEDBACK_NOT_FOUND(4050, HttpStatus.NOT_FOUND, "Feedback not found. The specified feedback ID does not exist in the system."),
    FEEDBACK_ORDER_NOT_BELONGS_TO_CUSTOMER(4051, HttpStatus.BAD_REQUEST, "Order does not belong to current customer. You can only review orders that belong to your account."),
    FEEDBACK_ONLY_COMPLETED_ORDERS(4052, HttpStatus.BAD_REQUEST, "Can only review completed orders. Please wait until your order is completed before leaving feedback."),

    // Ranking errors (4100-4199)
    RANKING_NOT_FOUND(4100, HttpStatus.NOT_FOUND, "Ranking not found. The specified ranking ID does not exist in the system."),

    // Payment errors (4200-4299)
    PAYMENT_FAILED(4200, HttpStatus.BAD_REQUEST, "Payment processing failed. Please check your payment information and try again."),
    PAYMENT_INVALID(4201, HttpStatus.BAD_REQUEST, "Invalid payment information provided. Please verify your payment details."),

    // Elasticsearch errors (4300-4399)
    ELASTICSEARCH_SEARCH_FAILED(4300, HttpStatus.INTERNAL_SERVER_ERROR, "Search service failed. The search operation could not be completed. Please try again later."),

    // Banner errors (4400-4499)
    BANNER_NOT_FOUND(4400, HttpStatus.NOT_FOUND, "Banner not found. The specified banner ID does not exist in the system."),

    // Article errors (4500-4599)
    ARTICLE_NOT_FOUND(4500, HttpStatus.NOT_FOUND, "Article not found. The specified article ID does not exist in the system."),
    ARTICLE_SLUG_NOT_FOUND(4501, HttpStatus.NOT_FOUND, "Article not found. The specified article slug does not exist in the system."),
    ARTICLE_TITLE_EXISTS(4502, HttpStatus.CONFLICT, "Article title already exists. An article with this title is already registered in the system."),
    ARTICLE_SLUG_EXISTS(4503, HttpStatus.CONFLICT, "Article slug already exists. An article with this slug is already registered in the system."),
    ARTICLE_CATEGORY_NOT_FOUND(4504, HttpStatus.NOT_FOUND, "Article category not found. The specified article category ID does not exist in the system."),
    ARTICLE_CATEGORY_SLUG_NOT_FOUND(4505, HttpStatus.NOT_FOUND, "Article category not found. The specified article category slug does not exist in the system."),
    ARTICLE_CATEGORY_TITLE_EXISTS(4506, HttpStatus.CONFLICT, "Article category title already exists. An article category with this title is already registered in the system."),
    ARTICLE_CATEGORY_SLUG_EXISTS(4507, HttpStatus.CONFLICT, "Article category slug already exists. An article category with this slug is already registered in the system."),

    // Role errors (4550-4559)
    ROLE_NOT_FOUND(4550, HttpStatus.NOT_FOUND, "Role not found. The specified role does not exist in the system."),

    // Staff errors (4560-4569)
    STAFF_NOT_FOUND(4560, HttpStatus.NOT_FOUND, "Staff not found. The specified staff ID does not exist in the system."),

    // User errors (4570-4579)
    USER_NOT_FOUND(4570, HttpStatus.NOT_FOUND, "User not found. The specified user ID does not exist in the system."),
    USER_NOT_FOUND_BY_EMAIL(4571, HttpStatus.NOT_FOUND, "User not found. The specified email does not exist in the system."),
    REFRESH_TOKEN_NOT_FOUND(4572, HttpStatus.NOT_FOUND, "Refresh token not found"),

    // Supplier errors (4580-4589)
    SUPPLIER_NOT_FOUND(4580, HttpStatus.NOT_FOUND, "Supplier not found. The specified supplier ID does not exist in the system."),
    SUPPLIER_INACTIVE(4581, HttpStatus.BAD_REQUEST, "Supplier is inactive. The specified supplier is not available for use."),
    SUPPLIER_PHONE_EXISTS(4582, HttpStatus.CONFLICT, "Phone number already exists. This phone number is already associated with another supplier."),

    // Purchase Order errors (4590-4599)
    PURCHASE_ORDER_NOT_FOUND(4590, HttpStatus.NOT_FOUND, "Purchase order not found. The specified purchase order ID does not exist in the system."),

    // Product Question errors (4595-4599)
    PRODUCT_QUESTION_NOT_FOUND(4595, HttpStatus.NOT_FOUND, "Product question not found. The specified product question ID does not exist in the system."),
    PRODUCT_QUESTION_ANSWER_NOT_FOUND(4596, HttpStatus.NOT_FOUND, "Product question answer not found. The specified answer ID does not exist in the system."),

    // Filter Criteria errors (4597-4599)
    FILTER_CRITERIA_NOT_FOUND(4597, HttpStatus.NOT_FOUND, "Filter criteria not found. The specified filter criteria ID does not exist in the system."),

    // Attribute errors (4598-4599)
    ATTRIBUTE_NOT_FOUND(4598, HttpStatus.NOT_FOUND, "Attribute not found. The specified attribute ID does not exist in the system."),

    // Authentication errors (2004-2010)
    PASSWORD_MISMATCH(2004, HttpStatus.BAD_REQUEST, "Password and confirm password do not match. Please ensure both passwords are identical."),
    EMAIL_ALREADY_REGISTERED(2005, HttpStatus.CONFLICT, "Email already registered. This email address is already associated with an account."),
    PHONE_ALREADY_REGISTERED(2006, HttpStatus.CONFLICT, "Phone already registered. This phone number is already associated with an account."),
    REFRESH_TOKEN_NOT_FOUND_IN_COOKIES(2007, HttpStatus.UNAUTHORIZED, "Missing refresh token"),
    REFRESH_TOKEN_INVALID(2008, HttpStatus.UNAUTHORIZED, "Invalid refresh token"),
    REFRESH_TOKEN_NOT_BELONGS_TO_USER(2009, HttpStatus.UNAUTHORIZED, "Invalid refresh token"),
    REFRESH_TOKEN_REVOKED(2010, HttpStatus.UNAUTHORIZED, "Refresh token revoked"),
    REFRESH_TOKEN_EXPIRED(2011, HttpStatus.UNAUTHORIZED, "Refresh token expired"),
    USER_DISABLED(2012, HttpStatus.UNAUTHORIZED, "User is disabled. Your account has been deactivated. Please contact support."),
    TOKEN_NOT_BELONGS_TO_USER(2013, HttpStatus.FORBIDDEN, "Token does not belong to current user. Please login again."),
    INVALID_PASSWORD(2014, HttpStatus.UNAUTHORIZED, "Invalid password. Please check your password and try again."),
    UNSUPPORTED_LOGIN_TYPE(2015, HttpStatus.BAD_REQUEST, "Unsupported login type. Please use a valid login method."),

    // Staff errors (4560-4569)
    STAFF_EMAIL_EXISTS(4561, HttpStatus.CONFLICT, "Email already exists. This email address is already associated with another staff account."),
    ROLE_ID_CANNOT_BE_NULL(4562, HttpStatus.BAD_REQUEST, "Role ID cannot be null. Please provide a valid role ID."),

    // Voucher errors (3400-3499) - additional
    VOUCHER_CODE_EXISTS(3405, HttpStatus.CONFLICT, "Voucher code already exists. A voucher with this code is already registered in the system."),
    VOUCHER_CANNOT_REMOVE_CUSTOMERS(3406, HttpStatus.CONFLICT, "Cannot remove customers who have already received the voucher. These customers cannot be removed from the voucher list."),
    VOUCHER_NOT_ACTIVE_YET(3407, HttpStatus.BAD_REQUEST, "Voucher is not active yet, cannot send. Please wait until the voucher becomes active."),
    VOUCHER_ALL_REQUIRES_CODE(3408, HttpStatus.BAD_REQUEST, "Voucher type 'ALL' requires a code. Please provide a voucher code."),
    VOUCHER_GROUP_REQUIRES_CUSTOMERS(3409, HttpStatus.BAD_REQUEST, "Voucher type 'GROUP' requires a customer list. Please provide at least one customer."),
    VOUCHER_RANK_REQUIRES_RANK_ID(3410, HttpStatus.BAD_REQUEST, "Voucher type 'RANK' requires a rankId. Please provide a valid ranking ID."),
    INVALID_VOUCHER_TYPE(3411, HttpStatus.BAD_REQUEST, "Invalid voucher type. Must be one of: ALL, GROUP, RANK."),

    // Feedback errors (4050-4059) - additional
    FEEDBACK_ALREADY_EXISTS(4053, HttpStatus.BAD_REQUEST, "You have already reviewed this product for this order. You can only leave one review per product per order."),
    FEEDBACK_PRODUCT_NOT_IN_ORDER(4054, HttpStatus.BAD_REQUEST, "Product variant is not in this order. You can only review products that are part of this order."),

    // Upload errors (4800-4899) - additional
    UPLOAD_FILENAME_EMPTY(4803, HttpStatus.BAD_REQUEST, "File name cannot be empty. Please provide a valid file name."),
    UPLOAD_FILE_LIST_EMPTY(4804, HttpStatus.BAD_REQUEST, "File list cannot be null or empty. Please provide at least one file."),
    UPLOAD_FILE_NULL(4805, HttpStatus.BAD_REQUEST, "File cannot be null or empty. Please select a valid file."),
    UPLOAD_FILENAME_NULL(4806, HttpStatus.BAD_REQUEST, "File name cannot be null. Please provide a valid file name."),

    // Excel Import/Export errors (4600-4699)
    EXCEL_FILE_EMPTY(4600, HttpStatus.BAD_REQUEST, "File is null or empty! Please select a valid Excel file."),
    EXCEL_TEMPLATE_GENERATION_FAILED(4601, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate Excel template."),
    EXCEL_IMPORT_FAILED(4602, HttpStatus.INTERNAL_SERVER_ERROR, "Import failed due to an internal error."),
    EXCEL_EXPORT_FAILED(4603, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to export data to Excel."),

    // Email errors (4700-4799)
    EMAIL_ADDRESS_NOT_FOUND(4700, HttpStatus.BAD_REQUEST, "Email address not found. The specified email address does not exist or is invalid."),
    EMAIL_SEND_FAILED(4701, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send email. Please try again later or contact support if the problem persists."),

    // Upload errors (4800-4899)
    UPLOAD_FAILED(4800, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload file. Please try again later or contact support if the problem persists."),
    UPLOAD_FILE_NOT_FOUND(4801, HttpStatus.NOT_FOUND, "File not found. The specified file does not exist in the system."),
    DELETE_FILE_FAILED(4802, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete file. Please try again later or contact support if the problem persists.");

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(int code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public static ErrorCode fromCode(int code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.code == code) {
                return errorCode;
            }
        }
        return INTERNAL_SERVER_ERROR;
    }
}

