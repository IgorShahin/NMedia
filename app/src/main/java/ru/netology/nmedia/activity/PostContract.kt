package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import ru.netology.nmedia.dto.Post

class PostContract : ActivityResultContract<Post?, Post?>() {

    override fun createIntent(context: Context, input: Post?): Intent {
        return Intent(context, PostHandlerActivity::class.java)
            .putExtra(EXTRA_POST, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Post? {
        return if (resultCode == Activity.RESULT_OK) {
            intent?.getParcelableExtra(EXTRA_POST) as? Post
        } else {
            null
        }
    }

    companion object {
        const val EXTRA_POST = "EXTRA_POST"
    }
}