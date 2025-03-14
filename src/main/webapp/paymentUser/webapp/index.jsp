<%@ page import="dto.UsersDTO" %>
<%@ page import="dto.PaymentItemDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="com.google.gson.reflect.TypeToken" %>
<%@ page import="dto.SaleDTO" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>결제페이지</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, "Malgun Gothic", "맑은 고딕", helvetica, sans-serif;
            background-color: #f5f6f7;
            color: #333;
        }

        .container {
            max-width: 720px;
            margin: 0 auto;
            padding: 20px;
        }

        .nav {
            display: flex;
            gap: 20px;
            padding: 15px 0;
            border-bottom: 1px solid #e5e5e5;
            margin-bottom: 30px;
        }

        .nav-item {
            cursor: pointer;
            color: #666;
        }

        .nav-item.active {
            color: #1a73e8;
            font-weight: bold;
        }

        .section-title {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 30px;
            text-align: center;
        }

        .payment-methods {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
        }

        .payment-method {
            flex: 1;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 4px;
            text-align: center;
            cursor: pointer;
        }

        .payment-method.selected {
            border-color: #1a73e8;
            background-color: #f8f9ff;
        }

        .price-details {
            background-color: white;
            padding: 20px;
            border-radius: 4px;
            margin-bottom: 20px;
        }

        .price-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
            color: #666;
        }

        .price-row.total {
            color: #ff4a4a;
            font-weight: bold;
            font-size: 18px;
            border-top: 1px solid #eee;
            padding-top: 10px;
            margin-top: 10px;
        }

        .terms-checkbox {
            margin-bottom: 20px;
        }

        .submit-button {
            width: 100%;
            padding: 15px;
            background-color: #1a73e8;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
        }

        .order-item {
            background-color: white;
            border-radius: 4px;
            padding: 20px;
            margin-bottom: 20px;
        }

        .order-header {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
        }

        .order-title {
            font-size: 18px;
            font-weight: bold;
            flex-grow: 1;
        }

        .delivery-badge {
            background-color: #f1f1f1;
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 14px;
        }

        .order-content {
            display: flex;
            gap: 20px;
        }

        .order-image {
            width: 100px;
            height: 100px;
            object-fit: cover;
            border-radius: 4px;
        }

        .order-details {
            flex-grow: 1;
        }

        .order-name {
            font-weight: bold;
            margin-bottom: 8px;
        }

        .order-option {
            color: #666;
            font-size: 14px;
            margin-bottom: 4px;
        }

        .order-price {
            font-size: 16px;
            font-weight: bold;
            color: #1a73e8;
            margin-top: 10px;
        }

        .discount-tag {
            display: inline-block;
            background-color: #fff4f4;
            color: #ff4a4a;
            padding: 2px 6px;
            border-radius: 4px;
            font-size: 12px;
            margin-left: 8px;
        }

        .user-edit {
            top: 20px;
            right: 20px;
            padding: 5px 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            background: whitesmoke;
            font-size: 14px;
            cursor: pointer;
        }

        .user-info {
            display: flex;
            flex-direction: column;
            gap: 8px;
        }

        .user-info-row {
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .user-info-value {
            font-size: 14px;
        }
    </style>
</head>
<body>

<script src="https://cdn.iamport.kr/v1/iamport.js"></script>
<script>
function formatDateToTimestamp(dateStr) {
    const date = new Date(dateStr);
    return date.toISOString().slice(0, 19).replace('T', ' ');
}

function payment() {
    // 예약 정보 수집
    // const itemId = document.getElementById('itemId').value;
    // const quantity = document.getElementById('quantity').value;
    // const totalPrice = document.querySelector('.price-row.total span:last-child')
    //     .textContent.replace(/[^0-9]/g, '');
    // const rentalshopId = document.getElementById('rentalshopId').value;
    // const rentalStart = document.getElementById('rental_start').value;
    // const rentalEnd = document.getElementById('rental_end').value;
    const quantity = 1;
    const totalPrice = 2;
    const rentalshopId = 21;
    const rentalStart = new Date().getTime();
    const rentalEnd = new Date().getTime();
    const sale_id = 1;
    const item_id = 21;
    const cart_id =21;

    var IMP = window.IMP;
    IMP.init('imp53707603');
    
    IMP.request_pay({
        pg: 'html5_inicis',
        pay_method: 'card',
        merchant_uid: 'merchant_' + new Date().getTime(),
        name: '주문명:결제테스트',
        amount: totalPrice,
        buyer_email: '2@naver.com',
        buyer_name: '김정현',
        buyer_tel:'010-6775-0571',
        buyer_addr: '주소',
        buyer_postcode: '우편번호',
    }, function (rsp) {
        if (rsp.success) {
            const paymentData = {
                imp_uid: rsp.imp_uid,
                total_price: rsp.paid_amount,
                payment_method: rsp.pay_method,
                card_name: rsp.card_name || "",
                card_number: rsp.card_number || "",
                uuid: 1,
                sale_id: sale_id,
                cart_id: cart_id,
                item_id: 21,
                reservationDetail: {
                    rentalshop_id: rentalshopId,
                    rental_start: rentalStart,
                    rental_end: rentalEnd,
                    item_id: item_id,
                    quantity: quantity,
                    price:totalPrice,
                }
            };

            // 서버에 결제 및 예약 정보 전송
            const xhr = new XMLHttpRequest();
            xhr.open('POST', '<%=request.getContextPath()%>/controller/payment', true);
            xhr.setRequestHeader('Content-Type', 'application/json');

            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4) {
                    try {
                        const response = JSON.parse(xhr.responseText);
                        if (xhr.status === 200 && response.success) {
                            alert(response.message);
                            location.href = response.redirect_url + '?payment_id=' + response.payment_id;

                        } else {
                            alert(response.message || '처리 중 오류가 발생했습니다.');
                        }
                    } catch (e) {
                        console.error('JSON 파싱 오류:', e);
                        alert('서버 응답을 처리하는 중 오류가 발생했습니다.');
                    }
                }
            };

            xhr.send(JSON.stringify(paymentData));
        } else {
            let msg = '결제에 실패하였습니다.';
            msg += '에러내용 : ' + rsp.error_msg;
            alert(msg);
        }
    });
}

function dateToLong(date) {
    if (typeof date === "number") {
        return date * 1000;
    }
    if (!(date instanceof Date)) {
        date = new Date(date);
    }
    return date.getTime();
}

function checkTermsAndPay() {
    const termsChecked = document.getElementById('terms').checked;
    
    if (!termsChecked) {
        alert('구매 조건 확인 및 결제 진행 동의가 필요합니다.');
        return;
    }
    
    payment();
}
</script>

<div class="container">
    <nav class="nav">
        <div class="nav-item active">스키장</div>
        <div class="nav-item">렌탈</div>
        <div class="nav-item">리조트</div>
        <div class="nav-item" style="margin-left: auto;">마이페이지</div>
        <div class="nav-item">장바구니</div>
    </nav>

    <h1 class="section-title">예약 확인 및 결제</h1>

    <div class="order-item">
        <div class="order-header">
            <div class="order-title">사용자 정보</div>
            <input type="button" class="user-edit" value="변경"/>
        </div>
        <div class="user-info">
            <div class="user-info-row">
                <span class="user-info-value">장민영</span>
            </div>
            <div class="user-info-row">
                <span class="user-info-value">010-8393-4282</span>
            </div>
        </div>
    </div>

        <div class="order-item">
            <div class="order-header">
                <div class="order-title">주문상품</div>
                <div class="delivery-badge">무료 배송</div>
            </div>
            <div class="order-content">
                <img src="1.png" alt="상품이미지" class="order-image">
                <div class="order-details">
                    <div class="order-name">스키장 패키지</div>
                    <div class="order-option">1 size / 1</div>
                    <div class="order-price">
                        10원
                        <span class="discount-tag">최대 적용 0원</span>
                    </div>
                </div>
            </div>
        </div>



    <div class="order-item">
        <div class="order-header">
            <div class="order-title">결제 정보</div>
        </div>
        <div class="price-details" style="background: none; padding: 0;">
            <div class="price-row">
                <span>제품 가격</span>
                <span>10원</span>
            </div>
            <div class="price-row">
                <span>할인/쿠폰</span>
                <span>-8원</span>
            </div>
            <div class="price-row total">
                <span>총 주문금액</span>
                <span>2원</span>
            </div>
        </div>
    </div>

    <div class="order-item">
        <div class="order-header">
            <div class="order-title">결제 수단</div>
        </div>
        <div class="payment-methods" style="margin: 0;">
            <div class="payment-method selected">
                <span>KG 이니시스</span>
            </div>
            <div class="payment-method">
                <span>토스페이먼츠</span>
            </div>
        </div>
    </div>

    <div class="terms-checkbox">
        <input type="checkbox" id="terms">
        <label for="terms">위 구매 조건 확인 및 결제 진행 동의</label>
    </div>

    <button onclick="checkTermsAndPay()" class="submit-button">
        2원 결제하기
    </button>
</div>

</body>
</html>
