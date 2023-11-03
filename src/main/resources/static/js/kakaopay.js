

$("#check_module").click(function () {
  var postal_code = document.getElementById("sample6_postcode").value;
  var detailed_address = document.getElementById("sample6_detailAddress").value;
  var name = document.getElementById("name").value;
  var email = document.getElementById("email").value;
  var totalPrice = document.getElementById("totalPrice").value;
  var phone = document.getElementById("phone").value;
  var middle_address = document.getElementById("sample6_address").value;
  var payment = $(":input:radio[name=payment]:checked").val();
  document.getElementsByClassName
  var IMP = window.IMP; // 생략가능
  IMP.init("imp44111882");
  // 'iamport' 대신 부여받은 "가맹점 식별코드"를 사용
  // i'mport 관리자 페이지 -> 내정보 -> 가맹점식별코드
  IMP.request_pay({
    pg: payment, // version 1.1.0부터 지원.
    /*
        'kakaopay':카카오페이,
        html5_inicis':이니시스(웹표준결제)
            'nice':나이스페이
            'jtnet':제이티넷
            'uplus':LG유플러스
            'danal':다날
            'payco':페이코
            'syrup':시럽페이
            'paypal':페이팔
        */

    pay_method: 'card',
    /*
        'samsung':삼성페이,
        'card':신용카드,
        'trans':실시간계좌이체,
        'vbank':가상계좌,
        'phone':휴대폰소액결제
    */

    merchant_uid: 'merchant_' + new Date().getTime(),
    /*
        merchant_uid에 경우
        https://docs.iamport.kr/implementation/payment
        위에 url에 따라가시면 넣을 수 있는 방법이 있습니다.
        참고하세요.
     */

    name: "UNCOMMON", //결제창에서 보여질 이름
    amount: totalPrice,  //가격
    buyer_email: email,
    buyer_name: name,
    buyer_tel: phone,
    buyer_addr: middle_address + detailed_address,
    buyer_postcode: postal_code,
    m_redirect_url: '/'
    /*
        모바일 결제시,
        결제가 끝나고 랜딩되는 URL을 지정
        (카카오페이, 페이코, 다날의 경우는 필요없음. PC와 마찬가지로 callback함수로 결과가 떨어짐)
        */
  }, function (rsp) {
    console.log(rsp);
    if (rsp.success) {
      var msg = '결제가 완료되었습니다.';
      msg += '고유ID : ' + rsp.imp_uid;
      msg += '상점 거래ID : ' + rsp.merchant_uid;
      msg += '결제 금액 : ' + rsp.paid_amount;
      msg += '카드 승인번호 : ' + rsp.apply_num;
      alert(msg);
      // window.location.href='/kakaopay';
      $.ajax({
        type: 'POST',
        url: '/kakaopay',
        dataType: "json",
        data : {
          email : email,
          payment : payment,
          totalPrice : totalPrice,
          name : name,
          phone : phone,
          middle_address : middle_address,
          detailed_address : detailed_address,
          postal_code : postal_code,
          'imp_uid' : rsp.imp_uid,
        },
        success: function (data) {
          // 서버에서 응답을 받았을 때의 처리를 수행
          window.location.href = data.redirectUrl; // 또는 다른 페이지로 이동
        },
        error: function (error) {
          // 에러 처리
          alert('POST 요청 중 에러가 발생했습니다.');
        }
      });
    } else {
      var msg = '결제에 실패하였습니다.';
      msg += '에러내용 : ' + rsp.error_msg;
    }
    alert(msg);
  });
});
