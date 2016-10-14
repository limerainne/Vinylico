package space.limerainne.i_bainil_u.data.api.request.data

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import space.limerainne.i_bainil_u.data.api.RecommendAlbumResponse
import space.limerainne.i_bainil_u.data.api.request.Request

/**
 * Created by CottonCandy on 2016-10-04.
 */

/*
- Request URL
http://www.bainil.com/api/v2/user/eml?userId=2543&&lang=ko
- Response example
{"result":{"albumEnglish":"뻘밭에서","albumName":"뻘밭에서","albumDesc":"이 시대 최후의 느와르 마초\r\n'불나방 스타 쏘세지 클럽' Single [뻘밭에서]\r\n \r\n'불나방 스타 쏘세지 클럽(줄여서 '불쏘클')'은 나약한 사나이들의 식어버린 청춘과 그로 인한 궁상에 치를 떨던 아티스트 '조 까를로스(노래, 기타)'를 구심점으로 그의 의지에 동의하는 여러 음악인이 모여 있는 신파와 정열의 느와르 마초 밴드이다. 우주를 구성한 3원소인 '불나방'과 '별', 그리고 '쏘세지'를 조합한 이름으로 2005년 만들어져 여러 번 멤버의 순환을 겪은 끝에 현재는 '조까를로스'를 비롯 '유미(드럼)', '까르푸황(베이스)', '김간지(멜로디언)'의 멤버로 구성되어 있다.\r\n \r\n'불쏘클'의 시작은 라틴 음악을 뿌리에 두고 훵크, 뽕짝, 판소리를 섭렵하는 다양한 장르를 섞은 후 신파와 야매의 기운을 곁들인 이른바 '얼터너티브 라틴 음악.' 하지만 그들의 진정한 정수는 음악적 스타일 이전에 '조 까를로스'가 집필하는 유머와 처연함, 폭력과 신파를 아무렇지 않게 섭렵해내는 인생의 이야기에 담겨있었다. 이를 통해 그들은 주로 혼자 자취하는 여대생을 중심으로 강력한 강력한 팬덤을 갖게 되었고, 더불어 인디 음악계에서도 열렬한 지지를 얻게 되었다.\r\n \r\n그리하여 '악어는 죽어서 가죽을 남기고 마초는 죽어서 콧수염을 남긴다'는 일성과 함께 2009년 EP [악어떼]와 1집 [고질적 신파]를 발매한 '불쏘클'은 각종 대형 페스티벌을 섭렵하는 한편 공중파 방송에마저 깃발을 꽂는 거침없는 행보를 보인다. 하지만 이렇게 한창 인기를 얻기 시작할 무렵인 2010년 9월, 그들은 [석연치 않은 결말] 이라는 실로 석연치 않은 제목의 EP를 남기고 돌연 은퇴하고 만다.\r\n \r\n하지만 그들의 인기는 도리어 은퇴 이후에 더욱 치솟고 만다. 1집의 타이틀 곡이었던 \"석봉아\"가 어느 오디션 프로그램에서 불려지면서 '불쏘클' 고유의 '민속 그루브'에 대한 대중의 관심이 치솟으며 덩달아 저작권 수입도 증가했고, 은퇴 직전에 남긴 마지막 노래 \"알앤비\"는 인상적인 TV 출연 영상으로 웹을 떠돌아다니며 전설로 자리매김하게 되었다. 이렇게 손 안 대고 코 푸는 듯한 상황에 그들의 복귀를 바라는 요청이 점점 거세졌으나, 정작 '조 까를로스'는 묵묵부답으로 일관하여 팬들의 마음은 까맣게 타들어 가고 만다.\r\n \r\n그렇게 시간은 계속 흘러갔고 이제 더 이상 타 들어갈 팬들의 가슴마저 모두 재가 되어버린 2013년의 어느 날, 갑작스럽게 '조 까를로스'는 복귀를 선언한다. 이 모든 해프닝이 결국에는 '조 까를로스' 作 '고질적 뮤지션의 길'에 포함되어 있는 시나리오를 따르는 행보라는 분석이 있지만, 어쨌든 그들은 녹색 감성의 본격 에코 힐링 밴드 '불쏘클 더 그레이스'라는 이름으로 페스티벌 무대로 돌아왔고, 곧이어 영화 '고령화 가족'의 OST에 '패티 김'의 \"초우\"를 리메이크한 것을 시작으로 창작 활동도 재개했다. 2013년 \"캠퍼스 포크송 대백과 사전\", 2014년 \"다 가질 걸 그랬어\", 2015년 \"고독사\"로 1년에 신곡 하나씩을 발표하며 겨우 연명해 오던 그들. 은퇴 선언 후 5년, 은퇴 번복 후로는 1년 6개월이 지난 2015년 10월, 2곡이 수록된 디지털 싱글 [뻘밭에서]를 출시하는 한편 몇 년 만인지도 모를 단독 콘서트를 같은 달 31일 개최하며 재활의 피치를 올리고 있다.","albumId":1267,"artistId":728,"fans":[{"userPic":"http://cloud.bainil.com/upload/user/3083/0be846d0-a2bc-44d0-9c96-13c344b545df_90.png","userId":3083,"userName":"kukumalu00","userRole":"ROLE_FAN"},{"userPic":"http://cloud.bainil.com/upload/user/3088/a5272b05-7c7a-4418-8987-e9f4e7bca3e2_90.png","userId":3088,"userName":"어제의카레","userRole":"ROLE_FAN"},{"userPic":"http://cloud.bainil.com/upload/user/1642/79c29304-3028-4c14-abc6-3dd084260fd6_90.png","userId":1642,"userName":"yahototoro","userRole":"ROLE_FAN"},{"userPic":"http://cloud.bainil.com/upload/user/5744/ab9827dc-2ce1-400c-8d86-2e296d7b418a_90.png","userId":5744,"userName":"jbw7436","userRole":"ROLE_FAN"},{"userPic":"http://cloud.bainil.com/upload/user/1425/03b3b59a-458b-4796-9b97-34b9d17746b4_90.png","userId":1425,"userName":"caodicblue","userRole":"ROLE_FAN"},{"userPic":"http://cloud.bainil.com/upload/user/5877/a628f570-e669-4776-b53a-aaf8c6c040ab_90.png","userId":5877,"userName":"Si-Hyun Kim","userRole":"ROLE_FAN"},{"userPic":"http://cloud.bainil.com/upload/user/5518/df1bfea0-728a-4fd8-86c0-bfd67d286b70_90.jpg","userId":5518,"userName":"layla1028","userRole":"ROLE_FAN"},{"userPic":"http://cloud.bainil.com/upload/user/2525/5d72c6aa-dbbe-4c64-97c6-e26dc25516e4_90.png","userId":2525,"userName":"lebongs","userRole":"ROLE_FAN"},{"userPic":"http://cloud.bainil.com/upload/user/6385/3fd0fc0d-3a0e-4007-b040-ab604b26ebc6_90.png","userId":6385,"userName":"badfellas","userRole":"ROLE_FAN"},{"userPic":"http://cloud.bainil.com/upload/user/2004/63cb7344-09f7-47e0-91b4-c4e0d2d77732_90.png","userId":2004,"userName":"imna413","userRole":"ROLE_FAN"}],"artistEnglish":"Bulnabang Star Sausage Club","albumDescEnglish":"이 시대 최후의 느와르 마초\r\n'불나방 스타 쏘세지 클럽' Single [뻘밭에서]\r\n \r\n'불나방 스타 쏘세지 클럽(줄여서 '불쏘클')'은 나약한 사나이들의 식어버린 청춘과 그로 인한 궁상에 치를 떨던 아티스트 '조 까를로스(노래, 기타)'를 구심점으로 그의 의지에 동의하는 여러 음악인이 모여 있는 신파와 정열의 느와르 마초 밴드이다. 우주를 구성한 3원소인 '불나방'과 '별', 그리고 '쏘세지'를 조합한 이름으로 2005년 만들어져 여러 번 멤버의 순환을 겪은 끝에 현재는 '조까를로스'를 비롯 '유미(드럼)', '까르푸황(베이스)', '김간지(멜로디언)'의 멤버로 구성되어 있다.\r\n \r\n'불쏘클'의 시작은 라틴 음악을 뿌리에 두고 훵크, 뽕짝, 판소리를 섭렵하는 다양한 장르를 섞은 후 신파와 야매의 기운을 곁들인 이른바 '얼터너티브 라틴 음악.' 하지만 그들의 진정한 정수는 음악적 스타일 이전에 '조 까를로스'가 집필하는 유머와 처연함, 폭력과 신파를 아무렇지 않게 섭렵해내는 인생의 이야기에 담겨있었다. 이를 통해 그들은 주로 혼자 자취하는 여대생을 중심으로 강력한 강력한 팬덤을 갖게 되었고, 더불어 인디 음악계에서도 열렬한 지지를 얻게 되었다.\r\n \r\n그리하여 '악어는 죽어서 가죽을 남기고 마초는 죽어서 콧수염을 남긴다'는 일성과 함께 2009년 EP [악어떼]와 1집 [고질적 신파]를 발매한 '불쏘클'은 각종 대형 페스티벌을 섭렵하는 한편 공중파 방송에마저 깃발을 꽂는 거침없는 행보를 보인다. 하지만 이렇게 한창 인기를 얻기 시작할 무렵인 2010년 9월, 그들은 [석연치 않은 결말] 이라는 실로 석연치 않은 제목의 EP를 남기고 돌연 은퇴하고 만다.\r\n \r\n하지만 그들의 인기는 도리어 은퇴 이후에 더욱 치솟고 만다. 1집의 타이틀 곡이었던 \"석봉아\"가 어느 오디션 프로그램에서 불려지면서 '불쏘클' 고유의 '민속 그루브'에 대한 대중의 관심이 치솟으며 덩달아 저작권 수입도 증가했고, 은퇴 직전에 남긴 마지막 노래 \"알앤비\"는 인상적인 TV 출연 영상으로 웹을 떠돌아다니며 전설로 자리매김하게 되었다. 이렇게 손 안 대고 코 푸는 듯한 상황에 그들의 복귀를 바라는 요청이 점점 거세졌으나, 정작 '조 까를로스'는 묵묵부답으로 일관하여 팬들의 마음은 까맣게 타들어 가고 만다.\r\n \r\n그렇게 시간은 계속 흘러갔고 이제 더 이상 타 들어갈 팬들의 가슴마저 모두 재가 되어버린 2013년의 어느 날, 갑작스럽게 '조 까를로스'는 복귀를 선언한다. 이 모든 해프닝이 결국에는 '조 까를로스' 作 '고질적 뮤지션의 길'에 포함되어 있는 시나리오를 따르는 행보라는 분석이 있지만, 어쨌든 그들은 녹색 감성의 본격 에코 힐링 밴드 '불쏘클 더 그레이스'라는 이름으로 페스티벌 무대로 돌아왔고, 곧이어 영화 '고령화 가족'의 OST에 '패티 김'의 \"초우\"를 리메이크한 것을 시작으로 창작 활동도 재개했다. 2013년 \"캠퍼스 포크송 대백과 사전\", 2014년 \"다 가질 걸 그랬어\", 2015년 \"고독사\"로 1년에 신곡 하나씩을 발표하며 겨우 연명해 오던 그들. 은퇴 선언 후 5년, 은퇴 번복 후로는 1년 6개월이 지난 2015년 10월, 2곡이 수록된 디지털 싱글 [뻘밭에서]를 출시하는 한편 몇 년 만인지도 모를 단독 콘서트를 같은 달 31일 개최하며 재활의 피치를 올리고 있다.","artistName":"불나방스타쏘세지클럽 ","jacketImage":"http://cloud.bainil.com/upload/album/1267/2bda2d83-0a72-4b61-aa1d-b0ce9b8cfa6f_360.jpg"},"success":true}
 */

class RequestRecommendAlbum(val userId: Long,
                         val lang: String = "ko",
                         val gson: Gson = Gson()) : Request {

    companion object    {
        private val URL = "http://www.bainil.com/api/v2/user/eml"
    }

    private fun composeURL(): String {
        var url = "${URL}?userId=$userId"
        url += "&lang=$lang"
        return url
    }

    override fun execute(): RecommendAlbumResponse {
        val RecommendAlbumResponseStr = java.net.URL(composeURL()).readText()
        return gson.fromJson<RecommendAlbumResponse>(RecommendAlbumResponseStr)
    }
}