<%--
  Created by IntelliJ IDEA.
  User: jhta
  Date: 2025-03-11
  Time: 오후 2:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>결제 환불</title>
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
            line-height: 1.5;
        }

        .container {
            max-width: 720px;
            margin: 50px auto;
            padding: 20px;
        }

        .refund-card {
            background-color: white;
            border-radius: 8px;
            padding: 40px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }

        .section-title {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 30px;
            text-align: center;
        }

        .payment-info {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 4px;
            margin-bottom: 30px;
        }

        .info-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
            padding: 10px 0;
            border-bottom: 1px solid #eee;
        }

        .info-row:last-child {
            border-bottom: none;
        }

        .info-label {
            color: #666;
        }

        .info-value {
            font-weight: bold;
        }

        .refund-reason {
            margin-bottom: 20px;
        }

        .refund-reason select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            margin-top: 5px;
        }

        .refund-button {
            width: 100%;
            padding: 15px;
            background-color: #ff4a4a;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            transition: background-color 0.2s;
        }

        .refund-button:hover {
            background-color: #ff3333;
        }

        .refund-button:disabled {
            background-color: #cccccc;
            cursor: not-allowed;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="refund-card">
        <h1 class="section-title">결제 환불</h1>
        
        <div class="payment-info">
            <div class="info-row">
                <span class="info-label">주문번호</span>
                <span class="info-value">28</span>
            </div>
            <div class="info-row">
                <span class="info-label">결제금액</span>
                <span class="info-value">
<%--                    <fmt:formatNumber value="${payment.total_price}" pattern="#,###"/>원--%>
                    2.0원
                </span>
            </div>
            <div class="info-row">
                <span class="info-label">결제수단</span>
                <span class="info-value">point</span>
            </div>
            <div class="info-row">
                <span class="info-label">결제일시</span>
                <span class="info-value">

                </span>
            </div>
        </div>

        <div class="refund-reason">
            <label for="reason">환불 사유</label>
            <select id="reason" required>
                <option value="">환불 사유를 선택해주세요</option>
                <option value="고객변심">고객변심</option>
                <option value="상품불량">상품불량</option>
                <option value="기타">기타</option>
            </select>
        </div>

        <button onclick="requestRefund()" class="refund-button" id="refundButton">
            환불 신청하기
        </button>
    </div>
</div>

<script src="https://cdn.iamport.kr/v1/iamport.js"></script>
<script>
    function requestRefund() {
        const reason = document.getElementById('reason').value;
        if (!reason) {
            alert('환불 사유를 선택해주세요.');
            return;
        }

        if (!confirm('정말로 환불을 진행하시겠습니까?')) {
            return;
        }

        const refundButton = document.getElementById('refundButton');
        refundButton.disabled = true;

        // 포트원 객체 초기화
        var IMP = window.IMP;
        IMP.init('imp53707603');

        // 환불 요청 데이터
        const refundData = {
            imp_uid: '${payment.imp_uid}',        // 포트원 거래 고유번호
            merchant_uid: '${payment.merchant_uid}', // 가맹점 주문번호
            amount: 2.0,       // 환불할 금액
            reason: reason,                       // 환불 사유
            checksum: 2.0     // 환불 가능 금액
        };

        // 서버에 환불 요청
        const xhr = new XMLHttpRequest();
        xhr.open('POST', '<%=request.getContextPath()%>/controller/refund', true);
        xhr.setRequestHeader('Content-Type', 'application/json');

        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    alert('환불이 성공적으로 처리되었습니다.');
                    window.location.href = '<%=request.getContextPath()%>/';
                } else {
                    alert('환불 처리 중 오류가 발생했습니다.');
                    refundButton.disabled = false;
                }
            }
        };

        xhr.send(JSON.stringify(refundData));
    }
</script>
</body>
</html>
