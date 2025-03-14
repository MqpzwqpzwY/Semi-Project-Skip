<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>결제 완료</title>
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

        .success-card {
            background-color: white;
            border-radius: 8px;
            padding: 40px;
            text-align: center;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }

        .success-icon {
            width: 80px;
            height: 80px;
            background-color: #1a73e8;
            border-radius: 50%;
            margin: 0 auto 20px;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 40px;
        }

        .success-title {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 20px;
            color: #1a73e8;
        }

        .success-message {
            font-size: 16px;
            color: #666;
            margin-bottom: 30px;
        }

        .payment-info {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 4px;
            margin-bottom: 30px;
            text-align: left;
        }

        .payment-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
            padding: 10px 0;
            border-bottom: 1px solid #eee;
        }

        .payment-row:last-child {
            border-bottom: none;
        }

        .payment-label {
            color: #666;
        }

        .payment-value {
            font-weight: bold;
        }

        .home-button {
            background-color: #1a73e8;
            color: white;
            border: none;
            padding: 15px 40px;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
            transition: background-color 0.2s;
            text-decoration: none;
            display: inline-block;
        }

        .home-button:hover {
            background-color: #1557b0;
        }
    </style>
</head>
<body>

    <div class="container">
        <div class="success-card">
            <div class="success-icon">✓</div>
            <h1 class="success-title">결제가 완료되었습니다</h1>
            <p class="success-message">
                결제가 성공적으로 처리되었습니다.<br>
                이용해 주셔서 감사합니다.
            </p>

            <div class="payment-info">
                <div class="payment-row">
                    <span class="payment-label">주문번호</span>
                    <span class="payment-value" id="payment-id"></span>
                </div>
                <div class="payment-row">
                    <span class="payment-label">결제금액</span>
                    <span class="payment-value" id="total-price"></span>
                </div>
                <div class="payment-row">
                    <span class="payment-label">결제수단</span>
                    <span class="payment-value" id="payment-method"></span>
                </div>
                <div class="payment-row">
                    <span class="payment-label">결제상품</span>
                    <span class="payment-value" id="item-name"></span>
                </div>
                <div class="payment-row">
                    <span class="payment-label">결제일시</span>
                    <span class="payment-value" id="create-at"></span>
                </div>
            </div>

            <a href="<%=request.getContextPath()%>/" class="home-button">
                홈으로 돌아가기
            </a>
        </div>
    </div>

    <script>
        // 페이지 로드 시 실행
            window.onload = function() {
                const urlParams = new URLSearchParams(window.location.search);
                const paymentId = urlParams.get('payment_id');
                console.log(paymentId);
                fetch('<%=request.getContextPath()%>/controller/payment?payment_id=' + paymentId)
                        .then(async response => {
                            console.log('Response status:', response.status);

                            // 응답 본문을 텍스트로 먼저 확인
                            const text = await response.text();
                            console.log('Raw response text:', text);
                            if (text) {
                                try {
                                    const data = JSON.parse(text);
                                    console.log('Parsed JSON:', data);
                                    return data;
                                } catch (e) {
                                    console.error('JSON 파싱 에러:', e);
                                    throw new Error('Invalid JSON response');
                                }
                            } else {
                                throw new Error('Empty response');
                            }
                        })
                        .then(data => {
                            if (data.success) {
                                // 결제 정보 표시
                                document.getElementById('payment-id').textContent = data.payment_id;

                                // 금액 포맷팅
                                const formattedPrice = new Intl.NumberFormat('ko-KR').format(data.total_price);
                                document.getElementById('total-price').textContent = formattedPrice + '원';

                                document.getElementById('payment-method').textContent = data.payment_method;

                                // 날짜 포맷팅
                                const createDate = new Date(data.create_at);
                                const formattedDate = new Intl.DateTimeFormat('ko-KR', {
                                    year: 'numeric',
                                    month: '2-digit',
                                    day: '2-digit',
                                    hour: '2-digit',
                                    minute: '2-digit',
                                    second: '2-digit'
                                }).format(createDate);
                                document.getElementById('create-at').textContent = formattedDate;

                                // 결제상품 정보가 있다면
                                if (data.name) {
                                    document.getElementById('item-name').textContent = data.name;
                                }
                            }
                        })
                        .catch(error => {
                            console.error('Error:', error);
                            alert('결제 정보를 불러오는데 실패했습니다.');
                        });
                }

    </script>
</body>
</html>
