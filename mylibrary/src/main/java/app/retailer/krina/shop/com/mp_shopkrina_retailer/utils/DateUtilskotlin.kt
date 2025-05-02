package app.retailer.krina.shop.com.mp_shopkrina_retailer.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.StaticLayout
import android.text.TextPaint
import android.text.format.DateUtils
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.TextView
import android.widget.TextView.BufferType
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt


class DateUtilskotlin {

    companion object {

        fun String.isValidUrl(): Boolean = Patterns.WEB_URL.matcher(this).matches()
        fun dateConvertToTime(dataDate: String?): String {
            var convTime: String = ""
                                           "2023-08-22T11:22:14.314Z"
            if (!dataDate.isNullOrEmpty()) {"2023-08-22T16:16:19.327Z"
               // val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss.SSS'Z'", Locale.ENGLISH)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX", Locale.ENGLISH)
                dateFormat.timeZone = TimeZone.getTimeZone("UTC")

                try {
                    val date = dateFormat.parse(dataDate)
                    val targetTimestamp = date.time
                    val currentTimestamp = System.currentTimeMillis()
                    val timeAgo = DateUtils.getRelativeTimeSpanString(
                        targetTimestamp,
                        currentTimestamp,
                        DateUtils.SECOND_IN_MILLIS
                    ).toString()
                    convTime = timeAgo
                } catch (e: ParseException) {
                    e.printStackTrace()
                    // Handle parsing exception
                }
            }
            return convTime
        }
        fun notifictiondateConvertToTime(dataDate: String?): String {
            var convTime: String = ""
                                           "2023-08-22T11:22:14.314Z"
            if (!dataDate.isNullOrEmpty()) {"2023-08-22T16:16:19.327Z"
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ENGLISH)
                //val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX", Locale.ENGLISH)
                dateFormat.timeZone = TimeZone.getTimeZone("UTC")

                try {
                    val date = dateFormat.parse(dataDate)
                    val targetTimestamp = date.time
                    val currentTimestamp = System.currentTimeMillis()
                    val timeAgo = DateUtils.getRelativeTimeSpanString(
                        targetTimestamp,
                        currentTimestamp,
                        DateUtils.SECOND_IN_MILLIS
                    ).toString()
                    convTime = timeAgo
                } catch (e: ParseException) {
                    e.printStackTrace()
                    // Handle parsing exception
                }
            }
            return convTime
        }
        fun commentConvertToTime(dataDate: String?): String {
            var convTime: String = ""
                                           "2023-08-22T11:22:14.314Z"
            if (!dataDate.isNullOrEmpty()) {"2023-08-22T16:16:19.327Z"
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX", Locale.ENGLISH)
               // dateFormat.timeZone = TimeZone.getTimeZone("UTC")

                try {
                    val date = dateFormat.parse(dataDate)
                    val targetTimestamp = date.time
                    val currentTimestamp = System.currentTimeMillis()
                    val timeAgo = DateUtils.getRelativeTimeSpanString(
                        targetTimestamp,
                        currentTimestamp,
                        DateUtils.MINUTE_IN_MILLIS
                    ).toString()
                    convTime = timeAgo
                } catch (e: ParseException) {
                    e.printStackTrace()
                    // Handle parsing exception
                }
            }
            return convTime
        }

        fun getPath(context: Context, uri: Uri?): String? {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor =
                context.contentResolver.query(uri!!, projection, null, null, null)
                    ?: return null
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val s = cursor.getString(column_index)
            cursor.close()
            return s
        }
        fun setHighLightedText(tv: TextView, textToHighlight: String, black: Int) {
            val tvt = tv.text.toString()
            var ofe = tvt.indexOf(textToHighlight, 0)
            val wordToSpan: Spannable = SpannableString(tv.text)
            var ofs = 0
            while (ofs < tvt.length && ofe != -1) {
                ofe = tvt.indexOf(textToHighlight, ofs)
                if (ofe == -1) break else {
                    // set color here
                    wordToSpan.setSpan(
                        BackgroundColorSpan(black),
                        ofe,
                        ofe + textToHighlight.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    tv.setText(wordToSpan, TextView.BufferType.SPANNABLE)
                }
                ofs = ofe + 1
            }
        }


        fun TextView.setResizableText(
            fullText: String,
            maxLines: Int,
            viewMore: Boolean,
            applyExtraHighlights: ((Spannable) -> (Spannable))? = null
        ) {
            val width = width
            if (width <= 0) {
                post {
                    setResizableText(fullText, maxLines, viewMore, applyExtraHighlights)
                }
                return
            }
            movementMethod = LinkMovementMethod.getInstance()
            // Since we take the string character by character, we don't want to break up the Windows-style
            // line endings.
            val adjustedText = fullText.replace("\r\n", "\n")
            // Check if even the text has to be resizable.
            val textLayout = StaticLayout(
                adjustedText,
                paint,
                width - paddingLeft - paddingRight,
                Layout.Alignment.ALIGN_NORMAL,
                lineSpacingMultiplier,
                lineSpacingExtra,
                includeFontPadding
            )
            if (textLayout.lineCount <= maxLines || adjustedText.isEmpty()) {
                val htmlText = adjustedText.replace("\n", "<br/>")
                text = addClickablePartTextResizable(
                    fullText,
                    maxLines,
                    HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
                    null,
                    viewMore,
                    applyExtraHighlights
                )
                return
            }
            val charactersAtLineEnd = textLayout.getLineEnd(maxLines - 1)
            val suffixText =
                if (viewMore) "..Read More" else ".Read Less"
            var charactersToTake = charactersAtLineEnd - suffixText.length / 2 // Good enough first guess
            if (charactersToTake <= 0) {
                // Happens when text is empty
                val htmlText = adjustedText.replace("\n", "<br/>")
                text = addClickablePartTextResizable(
                    fullText,
                    maxLines,
                    HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
                    null,
                    viewMore,
                    applyExtraHighlights
                )
                return
            }
            if (!viewMore) {
                // We can set the text immediately because nothing needs to be measured
                val htmlText = adjustedText.replace("\n", "<br/>")
                text = addClickablePartTextResizable(
                    fullText,
                    maxLines,
                    HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
                    suffixText,
                    viewMore,
                    applyExtraHighlights
                )
                return
            }
            val lastHasNewLine =
                adjustedText.substring(textLayout.getLineStart(maxLines - 1), textLayout.getLineEnd(maxLines - 1))
                    .contains("\n")
            var linedText = if (lastHasNewLine) {
                val charactersPerLine =
                    textLayout.getLineEnd(0) / (textLayout.getLineWidth(0) / textLayout.ellipsizedWidth.toFloat())
                val lineOfSpaces =
                    "\u00A0".repeat(charactersPerLine.roundToInt()) // non breaking space, will not be thrown away by HTML parser
                charactersToTake += lineOfSpaces.length - 1
                adjustedText.take(textLayout.getLineStart(maxLines - 1)) +
                        adjustedText.substring(textLayout.getLineStart(maxLines - 1), textLayout.getLineEnd(maxLines - 1))
                            .replace("\n", lineOfSpaces) +
                        adjustedText.substring(textLayout.getLineEnd(maxLines - 1))
            } else {
                adjustedText
            }
            // Check if we perhaps need to even add characters? Happens very rarely, but can be possible if there was a long word just wrapped
            val shortenedString = linedText.take(charactersToTake)
            val shortenedStringWithSuffix = shortenedString + suffixText
            val shortenedStringWithSuffixLayout = StaticLayout(
                shortenedStringWithSuffix,
                paint,
                width - paddingLeft - paddingRight,
                Layout.Alignment.ALIGN_NORMAL,
                lineSpacingMultiplier,
                lineSpacingExtra,
                includeFontPadding
            )
            val modifier: Int
            if (shortenedStringWithSuffixLayout.getLineEnd(maxLines - 1) >= shortenedStringWithSuffix.length) {
                modifier = 1
                charactersToTake-- // We might just be at the right position already
            } else {
                modifier = -1
            }
            do {
                charactersToTake += modifier
                val baseString = linedText.take(charactersToTake)
                val appended = baseString + suffixText
                val newLayout = StaticLayout(
                    appended,
                    paint,
                    width - paddingLeft - paddingRight,
                    Layout.Alignment.ALIGN_NORMAL,
                    lineSpacingMultiplier,
                    lineSpacingExtra,
                    includeFontPadding
                )
            } while ((modifier < 0 && newLayout.getLineEnd(maxLines - 1) < appended.length) ||
                (modifier > 0 && newLayout.getLineEnd(maxLines - 1) >= appended.length)
            )
            if (modifier > 0) {
                charactersToTake-- // We went overboard with 1 char, fixing that
            }
            // We need to convert newlines because we are going over to HTML now
            val htmlText = linedText.take(charactersToTake).replace("\n", "<br/>")
            text = addClickablePartTextResizable(
                fullText,
                maxLines,
                HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
                suffixText,
                viewMore,
                applyExtraHighlights
            )
        }

        private fun TextView.addClickablePartTextResizable(
            fullText: String,
            maxLines: Int,
            shortenedText: Spanned,
            clickableText: String?,
            viewMore: Boolean,
            applyExtraHighlights: ((Spannable) -> (Spannable))? = null
        ): Spannable {
            val builder = SpannableStringBuilder(shortenedText)
            if (clickableText != null) {
                builder.append(clickableText)
                val startIndexOffset = if (viewMore) 2 else 0 // Do not highlight the 3 dots and the space
                builder.setSpan(object : NoUnderlineClickSpan(context) {
                   override fun onClick(widget: View) {
                        if (viewMore) {
                            setResizableText(fullText, maxLines, false, applyExtraHighlights)
                        } else {
                            setResizableText(fullText, maxLines, true, applyExtraHighlights)
                        }
                    }
                }, builder.indexOf(clickableText) + startIndexOffset, builder.indexOf(clickableText) + clickableText.length, 0)
            }
            if (applyExtraHighlights != null) {
                return applyExtraHighlights(builder)
            }
            return builder
        }

        open class NoUnderlineClickSpan(val context: Context) : ClickableSpan() {
            override fun updateDrawState(textPaint: TextPaint) {
                textPaint.isUnderlineText = false
                textPaint.color = ContextCompat.getColor(context, R.color.colorAccent)
            }

            override fun onClick(widget: View) {}
        }

    }
}