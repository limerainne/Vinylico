package win.limerainne.vinylico.credential

import android.content.Context
import org.jetbrains.anko.toast
import win.limerainne.vinylico.R
import win.limerainne.vinylico.ThisApp
import win.limerainne.vinylico.extension.DelegatesExt
import win.limerainne.vinylico.extension.toLongOrElse
import win.limerainne.vinylico.view.MainActivity

/**
 * Created by Limerainne on 2016-09-28.
 */
class UserInfo(val context: Context) {

    var userId: Long by DelegatesExt.preference(context, "_userId", 0)

    var userImageURL: String by DelegatesExt.preference(context, "_userImageURL", "")
    val userImageURLFull: String
        get() = "http://cloud.bainil.com/upload/user" + userImageURL

    var userName: String by DelegatesExt.preference(context, "_userName", "")
    var userURL: String by DelegatesExt.preference(context, "_userURL", "")
    var userEmail: String by DelegatesExt.preference(context, "_userEmail", "")

    var facebookId: Long by DelegatesExt.preference(context, "_userFacebookId", 0)
    var twitterId: Long by DelegatesExt.preference(context, "_userTwitterId", 0)
    var instagramId: Long by DelegatesExt.preference(context, "_userInstagramId", 0)

    fun parseInfo(html: String) {
        /* example
  <input id="userId" name="userId" type="hidden" value="2"/>
  <div class="control-group">
    <label class="control-label">Picture</label>
    <div class="controls">
      <div class="fan-picture fileuploader imageupload" data-type="user">
        <div class="preview ">
          <img src="http://cloud.bainil.com/upload/user/2/5812_360.png"/>
          <input id="picture" name="picture" type="hidden" value="/2/5812_360.png"/>
        </div>
        <span class="fileinput-button">
          <span class="btn btn-small">Choose</span>
          <input type="file" name="file" class="input_file" data-url="/attach/image/uploader" />
        </span>
        <div class="progress progress-striped"><div class="bar"></div></div>
      </div>
      <span class="help-block help-custom">Recommended Size 360px x 360px</span>
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="name">Name</label>
    <div class="controls">
      <input id="name" name="name" class="required" placeHolder="Full Name" type="text" value="hello"/>
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="email">Email</label>
    <div class="controls">
      <input id="email" name="email" disabled="disabled" type="text" value="hello@bainil.com"/>
         */
        val inputRegex = Regex("""<input id=\"(.+?)\".*value=\"(.+?)\"""")

        val inputTags = inputRegex.findAll(html)

        for (inputTagEntry in inputTags)   {
            val id = inputTagEntry.groupValues[1]
            val value = inputTagEntry.groupValues[2]

            when    {
                id.equals("userId") -> userId = value.toLongOrElse(0)

                id.equals("picture") -> userImageURL = value

                id.equals("name") -> userName = value
                id.equals("url") -> userURL = value
                id.equals("email") -> userEmail = value

                id.equals("facebook") -> facebookId = value.toLongOrElse(0)
                id.equals("twitter") -> twitterId = value.toLongOrElse(0)
                id.equals("instagram") -> instagramId = value.toLongOrElse(0)
            }
        }

        // update App global UserID variable
        if(userId != 0L)
            ThisApp.get(context).updateUserId(userId)
    }

    fun clearInfo() {
        userId = 0

        userImageURL = ""

        userName = ""
        userURL = ""
        userEmail = ""

        facebookId = 0
        instagramId = 0
        twitterId = 0
    }

    override fun toString(): String {
        return "UserId: ${this.userId}" + " " +
                "UserName: ${this.userName}"
    }

    companion object    {
        fun checkLogin(context: Context): Boolean   {
            val userInfo = UserInfo(context)
            return userInfo.userId > 0
        }

        fun checkLoginThenRun4(context: Context, ok: () -> Unit, no: () -> Unit)    {
            if (!checkLogin(context))   {
                context.toast(context.getString(R.string.msg_login_required))
                
                no()
            }   else    {
                ok()
            }
        }

        fun checkLoginThenRun(context: Context, ok: () -> Unit, no: () -> Unit)    {
            if (!checkLogin(context))   {
                context.toast(context.getString(R.string.msg_login_required))

                val ctx = context
                if (ctx is MainActivity)
                    ctx.openLoginPage()

                no()
            }   else    {
                ok()
            }
        }

        fun getUserIdOr(context: Context): Long   {
            val userInfo = UserInfo(context)
            if (userInfo.userId > 0)
                return userInfo.userId
            else
                return ThisApp.USER_ID
        }

        fun getUserIdOrExcept(context: Context): Long   {
            val userInfo = UserInfo(context)
            if (userInfo.userId > 0)
                return userInfo.userId
            else
                throw RuntimeException()
        }
    }
}