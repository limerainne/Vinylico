package space.limerainne.i_bainil_u.data.api.request.data

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import space.limerainne.i_bainil_u.I_Bainil_UApp
import space.limerainne.i_bainil_u.data.api.SearchResultResponse
import space.limerainne.i_bainil_u.data.api.request.Request

/**
 * Created by Limerainne on 2016-07-21.
 */

/*
@ 16.10.13
- request example
http://www.bainil.com/api/v2/search?q=%EB%9F%AC%EB%B8%94%EB%A6%AC%EC%A6%88&userId=2543&store=1&lang=ko
-- NOTE
as in response, userId has no role in result; just for statistical issue?

- response structure
success: Boolean
result: Dict
  "artists", "albums", "tracks": List

- response type
"artists": if "artistName" has keyword?
"albums": if album, artist(?), ..., even album description has keyword?
"tracks": if artist, album, track has keyword?

- response example
{
   "result":{
      "artists":[
         {
            "albumEnglish":"Y",
            "artistPicture":"http://cloud.bainil.com/upload/artist/2659/37b35aa1-8181-4f54-a3c7-c808c964657d_180.jpg",
            "albumName":"Y",
            "albumId":3162,
            "artistId":2659,
            "artistEnglish":"Kei, myunDo",
            "artistName":"Kei (러블리즈), myunDo",
            "trackList":[
               {
                  "adult":0,
                  "songName":"Y (Feat. BUMZU)",
                  "songId":13490,
                  "songOrder":1
               }
            ]
         },
         {
            "albumEnglish":"A New Trilogy",
            "artistPicture":"http://cloud.bainil.com/upload/artist/1005/6851383d-45d7-4771-b300-18d4d5e9bc30_180.jpg",
            "albumName":"A New Trilogy",
            "albumId":2423,
            "artistId":1005,
            "artistEnglish":"Lovelyz",
            "artistName":"러블리즈",
            "trackList":[
               {
                  "adult":0,
                  "songName":"Moonrise",
                  "songId":10769,
                  "songOrder":1
               },
               {
                  "adult":0,
                  "songName":"Destiny (나의 지구)",
                  "songId":10770,
                  "songOrder":2
               },
               {
                  "adult":0,
                  "songName":"퐁당",
                  "songId":10771,
                  "songOrder":3
               },
               {
                  "adult":0,
                  "songName":"책갈피",
                  "songId":10772,
                  "songOrder":4
               },
               {
                  "adult":0,
                  "songName":"1cm",
                  "songId":10773,
                  "songOrder":5
               },
               {
                  "adult":0,
                  "songName":"마음 (*취급주의)",
                  "songId":10774,
                  "songOrder":6
               },
               {
                  "adult":0,
                  "songName":"인형",
                  "songId":10775,
                  "songOrder":7
               }
            ]
         }
      ],
      "albums":[
         {
            "albumEnglish":"INFINITE ONLY",
            "albumName":"INFINITE ONLY",
            "albumId":3238,
            "albumType":0,
            "artistEnglish":"Infinite",
            "eventUrl":"",
            "tracks":7,
            "free":false,
            "releaseDate":"2016-09-19",
            "artistId":2403,
            "event":false,
            "artistName":"인피니트 (Infinite) ",
            "trackList":[
               {
                  "adult":0,
                  "songName":"Eternity",
                  "songId":13913,
                  "songOrder":1
               },
               {
                  "adult":0,
                  "songName":"태풍 (The Eye)",
                  "songId":13914,
                  "songOrder":2
               },
               {
                  "adult":0,
                  "songName":"AIR",
                  "songId":13915,
                  "songOrder":3
               },
               {
                  "adult":0,
                  "songName":"One Day",
                  "songId":13916,
                  "songOrder":4
               },
               {
                  "adult":0,
                  "songName":"True Love",
                  "songId":13917,
                  "songOrder":5
               },
               {
                  "adult":0,
                  "songName":"고마워",
                  "songId":13918,
                  "songOrder":6
               },
               {
                  "adult":0,
                  "songName":"Zero",
                  "songId":13919,
                  "songOrder":7
               }
            ],
            "jacketImage":"http://cloud.bainil.com/upload/album/3238/3c64b141-5b93-45bc-9319-86c52adfa8e8_210.jpg"
         },
         {
            "albumEnglish":"Y",
            "albumName":"Y",
            "albumId":3162,
            "albumType":0,
            "artistEnglish":"Kei, myunDo",
            "eventUrl":"",
            "tracks":1,
            "free":false,
            "releaseDate":"2016-09-02",
            "artistId":2659,
            "event":false,
            "artistName":"Kei (러블리즈), myunDo",
            "trackList":[
               {
                  "adult":0,
                  "songName":"Y (Feat. BUMZU)",
                  "songId":13490,
                  "songOrder":1
               }
            ],
            "jacketImage":"http://cloud.bainil.com/upload/album/3162/142a4b1d-925e-4cb1-bba3-8b3179047e57_210.jpg"
         },
         {
            "albumEnglish":"아이돌보컬리그 - 걸스피릿 EPISODE 06",
            "albumName":"아이돌보컬리그 - 걸스피릿 EPISODE 06",
            "albumId":3147,
            "albumType":0,
            "artistEnglish":"Various Artists",
            "eventUrl":"",
            "tracks":3,
            "free":false,
            "releaseDate":"2016-08-24",
            "artistId":2649,
            "event":false,
            "artistName":"Various Artists",
            "trackList":[
               {
                  "adult":0,
                  "songName":"소정, 승희 - 24시간이 모자라 + NoNoNo",
                  "songId":13437,
                  "songOrder":1
               },
               {
                  "adult":0,
                  "songName":"유지 (베스티), Kei (러블리즈) - Twinkle + Something",
                  "songId":13438,
                  "songOrder":2
               },
               {
                  "adult":0,
                  "songName":"김보형 (스피카), 오승희 - 엄마",
                  "songId":13439,
                  "songOrder":3
               }
            ],
            "jacketImage":"http://cloud.bainil.com/upload/album/3147/55e6d1bc-f4fe-4f6a-a4c9-f181d407c601_210.jpg"
         },
         {
            "albumEnglish":"Second Love From The End OST PART 2",
            "albumName":"끝에서 두 번째 사랑 OST Part.2",
            "albumId":3000,
            "albumType":0,
            "artistEnglish":"끝에서 두 번째 사랑 OST",
            "eventUrl":"",
            "tracks":1,
            "free":false,
            "releaseDate":"2016-08-06",
            "artistId":2566,
            "event":false,
            "artistName":"끝에서 두 번째 사랑 OST",
            "trackList":[
               {
                  "adult":0,
                  "songName":"오늘도 맑음 (Clean)",
                  "songId":12854,
                  "songOrder":1
               }
            ],
            "jacketImage":"http://cloud.bainil.com/upload/album/3000/8dbe9cb5-17bc-4857-9379-0ce439da7f04_210.jpg"
         },
         {
            "albumEnglish":"운빨로맨스 OST",
            "albumName":"운빨로맨스 OST",
            "albumId":2864,
            "albumType":0,
            "artistEnglish":"운빨로맨스 OST",
            "eventUrl":"",
            "tracks":15,
            "free":false,
            "releaseDate":"2016-07-14",
            "artistId":2296,
            "event":false,
            "artistName":"운빨로맨스 OST",
            "trackList":[
               {
                  "adult":0,
                  "songName":"Kei (러블리즈) - 찌릿찌릿",
                  "songId":12416,
                  "songOrder":1
               },
               {
                  "adult":0,
                  "songName":"박혜수 - 슬픈인연",
                  "songId":12417,
                  "songOrder":2
               },
               {
                  "adult":0,
                  "songName":"소유 - 내게 말해줘",
                  "songId":12418,
                  "songOrder":3
               },
               {
                  "adult":0,
                  "songName":"다원 (우주소녀) - 샤랄라 로맨스",
                  "songId":12419,
                  "songOrder":4
               },
               {
                  "adult":0,
                  "songName":"스위트 피 - 진심을 너에게",
                  "songId":12420,
                  "songOrder":5
               },
               {
                  "adult":0,
                  "songName":"XIA (준수) - 내게 기대",
                  "songId":12421,
                  "songOrder":6
               },
               {
                  "adult":0,
                  "songName":"태윤 (TAEYOON) - 그 누구보다",
                  "songId":12422,
                  "songOrder":7
               },
               {
                  "adult":0,
                  "songName":"김은교 - Love 15",
                  "songId":12423,
                  "songOrder":8
               },
               {
                  "adult":0,
                  "songName":"소유 - 내게 말해줘 (Inst.)",
                  "songId":12424,
                  "songOrder":9
               },
               {
                  "adult":0,
                  "songName":"다원 (우주소녀) - 샤랄라 로맨스 (Inst.)",
                  "songId":12425,
                  "songOrder":10
               },
               {
                  "adult":0,
                  "songName":"XIA (준수) - 내게 기대 (Inst.)",
                  "songId":12426,
                  "songOrder":11
               },
               {
                  "adult":0,
                  "songName":"Trio Closer - Love 15 (Inst.)",
                  "songId":12427,
                  "songOrder":12
               },
               {
                  "adult":0,
                  "songName":"스위트 피 - LUCKY (Opening Title)",
                  "songId":12428,
                  "songOrder":13
               },
               {
                  "adult":0,
                  "songName":"스위트 피 - 찌릿찌릿 (Orgel Ver.)",
                  "songId":12429,
                  "songOrder":14
               },
               {
                  "adult":0,
                  "songName":"스위트 피 - 찌릿찌릿 (Trio Ver.)",
                  "songId":12430,
                  "songOrder":15
               }
            ],
            "jacketImage":"http://cloud.bainil.com/upload/album/2864/b3e95cb7-6af3-4253-bb68-128708f0fb09_210.jpg"
         },
         {
            "albumEnglish":"시작의 여름",
            "albumName":"시작의 여름",
            "albumId":2823,
            "albumType":0,
            "artistEnglish":"YoungJoon",
            "eventUrl":"",
            "tracks":1,
            "free":false,
            "releaseDate":"2016-07-07",
            "artistId":2400,
            "event":false,
            "artistName":"영준 (브라운 아이드 소울)",
            "trackList":[
               {
                  "adult":0,
                  "songName":"시작의 여름 (Duet With 류수정 of 러블리즈)",
                  "songId":12252,
                  "songOrder":1
               }
            ],
            "jacketImage":"http://cloud.bainil.com/upload/album/2823/af17249b-e736-4d8c-86d1-9d30c17e05e7_210.png"
         },
         {
            "albumEnglish":"Truly love",
            "albumName":"Truly love",
            "albumId":2613,
            "albumType":0,
            "artistEnglish":"SPACECOWBOY",
            "eventUrl":"",
            "tracks":2,
            "free":false,
            "releaseDate":"2016-05-26",
            "artistId":2236,
            "event":false,
            "artistName":"스페이스카우보이 (SPACECOWBOY)",
            "trackList":[
               {
                  "adult":0,
                  "songName":"Breakthrough!",
                  "songId":11448,
                  "songOrder":1
               },
               {
                  "adult":0,
                  "songName":"Grotesque (Feat. 팔로알토)",
                  "songId":11449,
                  "songOrder":2
               }
            ],
            "jacketImage":"http://cloud.bainil.com/upload/album/2613/61c03096-a44a-4b9f-bcd2-b2a61a47144d_210.jpg"
         },
         {
            "albumEnglish":"보컬전쟁 - 신의 목소리 Part.3",
            "albumName":"보컬전쟁 - 신의 목소리 Part.3",
            "albumId":2451,
            "albumType":0,
            "artistEnglish":"보컬전쟁 - 신의 목소리",
            "eventUrl":"",
            "tracks":3,
            "free":false,
            "releaseDate":"2016-04-28",
            "artistId":1752,
            "event":false,
            "artistName":"보컬전쟁 - 신의 목소리",
            "trackList":[
               {
                  "adult":0,
                  "songName":"박정현 - 심쿵해 (Heart Attack)",
                  "songId":10858,
                  "songOrder":1
               },
               {
                  "adult":0,
                  "songName":"거미 - 나는 나",
                  "songId":10859,
                  "songOrder":2
               },
               {
                  "adult":0,
                  "songName":"정인 - Ah-Choo",
                  "songId":10860,
                  "songOrder":3
               }
            ],
            "jacketImage":"http://cloud.bainil.com/upload/album/2451/cc91a57a-040d-4b41-9557-c036bbbcc57f_210.jpg"
         },
         {
            "albumEnglish":"A New Trilogy",
            "albumName":"A New Trilogy",
            "albumId":2423,
            "albumType":0,
            "artistEnglish":"Lovelyz",
            "eventUrl":"",
            "tracks":7,
            "free":false,
            "releaseDate":"2016-04-25",
            "artistId":1005,
            "event":false,
            "artistName":"러블리즈",
            "trackList":[
               {
                  "adult":0,
                  "songName":"Moonrise",
                  "songId":10769,
                  "songOrder":1
               },
               {
                  "adult":0,
                  "songName":"Destiny (나의 지구)",
                  "songId":10770,
                  "songOrder":2
               },
               {
                  "adult":0,
                  "songName":"퐁당",
                  "songId":10771,
                  "songOrder":3
               },
               {
                  "adult":0,
                  "songName":"책갈피",
                  "songId":10772,
                  "songOrder":4
               },
               {
                  "adult":0,
                  "songName":"1cm",
                  "songId":10773,
                  "songOrder":5
               },
               {
                  "adult":0,
                  "songName":"마음 (*취급주의)",
                  "songId":10774,
                  "songOrder":6
               },
               {
                  "adult":0,
                  "songName":"인형",
                  "songId":10775,
                  "songOrder":7
               }
            ],
            "jacketImage":"http://cloud.bainil.com/upload/album/2423/92b73ea3-a5c5-415e-a71b-fc05561d806d_210.jpg"
         },
         {
            "albumEnglish":"Fresh Adventure",
            "albumName":"Fresh Adventure",
            "albumId":2322,
            "albumType":0,
            "artistEnglish":"LABOUM",
            "eventUrl":"",
            "tracks":5,
            "free":false,
            "releaseDate":"2016-04-06",
            "artistId":2028,
            "event":false,
            "artistName":"라붐(LABOUM)",
            "trackList":[
               {
                  "adult":0,
                  "songName":"Intro",
                  "songId":10366,
                  "songOrder":1
               },
               {
                  "adult":0,
                  "songName":"상상더하기",
                  "songId":10367,
                  "songOrder":2
               },
               {
                  "adult":0,
                  "songName":"3 Strike Out",
                  "songId":10368,
                  "songOrder":3
               },
               {
                  "adult":0,
                  "songName":"Caterpillar",
                  "songId":10369,
                  "songOrder":4
               },
               {
                  "adult":0,
                  "songName":"상상더하기 (Inst.)",
                  "songId":10370,
                  "songOrder":5
               }
            ],
            "jacketImage":"http://cloud.bainil.com/upload/album/2322/8f9c1b0d-3ef1-4da6-ac5c-85b97f14181f_210.jpg"
         },
         {
            "albumEnglish":"No!Mantic",
            "albumName":"No!Mantic",
            "albumId":2000,
            "albumType":0,
            "artistEnglish":"이재훈, 김성수, 신지, 김종민, 빽가",
            "eventUrl":"",
            "tracks":1,
            "free":false,
            "releaseDate":"2016-02-12",
            "artistId":1758,
            "event":false,
            "artistName":"쿨요태",
            "trackList":[
               {
                  "adult":0,
                  "songName":"No!Mantic",
                  "songId":9001,
                  "songOrder":1
               }
            ],
            "jacketImage":"http://cloud.bainil.com/upload/album/2000/5a02d2d7-897f-4db2-aa11-0e3c4d3ff3e8_210.jpg"
         }
      ],
      "tracks":[
         {
            "albumId":3162,
            "artistId":2659,
            "artistEnglish":"Kei, myunDo",
            "adult":0,
            "songEnglish":"Y (Feat. BUMZU)",
            "songName":"Y (Feat. BUMZU)",
            "artistName":"Kei (러블리즈), myunDo",
            "songId":13490
         },
         {
            "albumId":3147,
            "artistId":2649,
            "artistEnglish":"Various Artists",
            "adult":0,
            "songEnglish":"유지 (베스티), Kei (러블리즈) - Twinkle + Something",
            "songName":"유지 (베스티), Kei (러블리즈) - Twinkle + Something",
            "artistName":"Various Artists",
            "songId":13438
         },
         {
            "albumId":3143,
            "artistId":2649,
            "artistEnglish":"Various Artists",
            "adult":0,
            "songEnglish":"탁재훈, Kei (러블리즈) - Kiss",
            "songName":"탁재훈, Kei (러블리즈) - Kiss",
            "artistName":"Various Artists",
            "songId":13428
         },
         {
            "albumId":2864,
            "artistId":2296,
            "artistEnglish":"운빨로맨스 OST",
            "adult":0,
            "songEnglish":"Kei (러블리즈) - 찌릿찌릿",
            "songName":"Kei (러블리즈) - 찌릿찌릿",
            "artistName":"운빨로맨스 OST",
            "songId":12416
         },
         {
            "albumId":2823,
            "artistId":2400,
            "artistEnglish":"YoungJoon",
            "adult":0,
            "songEnglish":"시작의 여름 (Duet With 류수정 of 러블리즈)",
            "songName":"시작의 여름 (Duet With 류수정 of 러블리즈)",
            "artistName":"영준 (브라운 아이드 소울)",
            "songId":12252
         },
         {
            "albumId":2423,
            "artistId":1005,
            "artistEnglish":"Lovelyz",
            "adult":0,
            "songEnglish":"인형",
            "songName":"인형",
            "artistName":"러블리즈",
            "songId":10775
         },
         {
            "albumId":2423,
            "artistId":1005,
            "artistEnglish":"Lovelyz",
            "adult":0,
            "songEnglish":"마음 (*취급주의)",
            "songName":"마음 (*취급주의)",
            "artistName":"러블리즈",
            "songId":10774
         },
         {
            "albumId":2423,
            "artistId":1005,
            "artistEnglish":"Lovelyz",
            "adult":0,
            "songEnglish":"1cm",
            "songName":"1cm",
            "artistName":"러블리즈",
            "songId":10773
         },
         {
            "albumId":2423,
            "artistId":1005,
            "artistEnglish":"Lovelyz",
            "adult":0,
            "songEnglish":"책갈피",
            "songName":"책갈피",
            "artistName":"러블리즈",
            "songId":10772
         },
         {
            "albumId":2423,
            "artistId":1005,
            "artistEnglish":"Lovelyz",
            "adult":0,
            "songEnglish":"퐁당",
            "songName":"퐁당",
            "artistName":"러블리즈",
            "songId":10771
         },
         {
            "albumId":2423,
            "artistId":1005,
            "artistEnglish":"Lovelyz",
            "adult":0,
            "songEnglish":"Destiny (나의 지구)",
            "songName":"Destiny (나의 지구)",
            "artistName":"러블리즈",
            "songId":10770
         },
         {
            "albumId":2423,
            "artistId":1005,
            "artistEnglish":"Lovelyz",
            "adult":0,
            "songEnglish":"Moonrise",
            "songName":"Moonrise",
            "artistName":"러블리즈",
            "songId":10769
         }
      ]
   },
   "success":true
}
 */
class RequestSearch(val keyword: String,
                    val userId: Long,
                    val store: Int = 1,
                    val lang: String = I_Bainil_UApp.LangCode,
                    val gson: Gson = Gson()) : Request {

    companion object    {
        private val URL = "http://www.bainil.com/api/v2/search"
    }

    private fun composeURL(): String {
        var url = "${URL}?q=$keyword"
        url += "&userId=$userId"
        url += "&store=$store&lang=$lang"
        return url
    }

    override fun execute(): SearchResultResponse {
        val searchResultJsonStr = java.net.URL(composeURL()).readText()
        return gson.fromJson<SearchResultResponse>(searchResultJsonStr)
    }
}