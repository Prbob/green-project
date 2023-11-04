function naverLogin(){
    var popup = window.open('https://nid.naver.com/nidlogin.login?oauth_token=iaezZYjVRis0VhhUce&consumer_key=LMW2HDBITKGDszfeHZr5&logintp=oauth2&nurl=https%3A%2F%2Fnid.naver.com%2Foauth2.0%2Fauthorize%3Fresponse_type%3Dtoken%26state%3D0e1174a0-390c-4f6b-a788-46e12c832d52%26client_id%3DLMW2HDBITKGDszfeHZr5%26redirect_uri%3Dhttp%253A%252F%252Flocalhost%253A8085%252FnaverLoginTest%26locale%3Dko_KR%26inapp_view%3D%26oauth_os%3D&locale=ko_KR&inapp_view=&svctype=1',
    '로그인팝업', 'width=700px,height=800px,scrollbars=yes');
  }

/*var naverLogin = new naver.LoginWithNaverId(
        {
            clientId: "LMW2HDBITKGDszfeHZr5", //내 애플리케이션 정보에 cliendId를 입력해줍니다.
            callbackUrl: "http://localhost:8085/naverLoginTest", // 내 애플리케이션 API설정의 Callback URL 을 입력해줍니다.
            isPopup: false,
            callbackHandle: true
        }
    );

naverLogin.init();

window.addEventListener('load', function () {
    naverLogin.getLoginStatus(function (status) {
        if (status) {
            var email = naverLogin.user.getEmail(); // 필수로 설정할것을 받아와 아래처럼 조건문을 줍니다.

            console.log(naverLogin.user);

            if( email == undefined || email == null) {
                alert("이메일은 필수정보입니다. 정보제공을 동의해주세요.");
                naverLogin.reprompt();
                return;
            }
        } else {
            console.log("callback 처리에 실패하였습니다.");
        }
    });
});


var testPopUp;
function openPopUp() {
    testPopUp= window.open("https://nid.naver.com/nidlogin.logout", "_blank", "toolbar=yes,scrollbars=yes,resizable=yes,width=1,height=1");
}
function closePopUp(){
    testPopUp.close();
}

function naverLogout() {
    openPopUp();
    setTimeout(function() {
        closePopUp();
        }, 1000);


}*/
