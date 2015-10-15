package ru.kraynov.app.ssaknitu.events.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Html;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.Touch;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.kraynov.app.ssaknitu.events.R;
import ru.kraynov.app.ssaknitu.events.util.helper.TypeFaceHelper;

public class EvTextView extends TextView {
    Context mContext;

    boolean dontConsumeNonUrlClicks = true;
    boolean linkHit;

    private ArrayList<Hyperlink> listOfLinks;
    TextLinkClickListener mListener;
    // Pattern for gathering @usernames from the Text
    Pattern screenNamePattern = Pattern.compile("(@[a-zA-Z0-9_]+)");
    // Pattern for gathering #hasttags from the Text
    Pattern hashTagsPattern = Pattern.compile("(#[a-zA-Z0-9_-]+)");
    // Pattern for gathering http:// links from the Text
    Pattern hyperLinksPattern = Pattern.compile("([Hh][tT][tT][pP][sS]?:\\/\\/[^ ,'\">\\]\\)]*[^\\. ,'\">\\]\\)])");

    public EvTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(attrs);
    }

    public EvTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);

    }

    public EvTextView(Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public void setHTMLTrimmed(String text){
        CharSequence trimmed = trim(Html.fromHtml(text));
        setText(trimmed);
    }

    public void setTextViewHTML(String html)
    {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        setText(strBuilder);
    }

    public static CharSequence trim(CharSequence source) {
        if(source == null)
            return "";

        int i = source.length();

        // loop back to the first non-whitespace character
        while(--i >= 0 && Character.isWhitespace(source.charAt(i))) {
        }

        return source.subSequence(0, i+1);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text.length() > 0) setVisibility(VISIBLE);
        else setVisibility(GONE);
    }

    private void init(AttributeSet attrs) {
        listOfLinks = new ArrayList<Hyperlink>();

        if (attrs!=null && !isInEditMode()) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.EvTextView);
            String fontName = a.getString(R.styleable.EvTextView_fontName);
            if (fontName!=null) {
                setTypeface(TypeFaceHelper.getTypeFace(mContext, fontName));
            }
            a.recycle();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        linkHit = false;
        boolean res = super.onTouchEvent(event);

        if (dontConsumeNonUrlClicks) return linkHit;

        return res;
    }

    public void gatherLinksForText(String text)
    {
        listOfLinks = new ArrayList<Hyperlink>();

        Spannable linkableText = new SpannableString(text);
    /*
     *  gatherLinks basically collects the Links depending upon the Pattern that we supply
     *  and add the links to the ArrayList of the links
     */
        gatherLinks(listOfLinks, linkableText, screenNamePattern);
        gatherLinks(listOfLinks, linkableText, hashTagsPattern);
        gatherLinks(listOfLinks, linkableText, Patterns.WEB_URL);

        for(int i = 0; i< listOfLinks.size(); i++)
        {
            Hyperlink linkSpec = listOfLinks.get(i);
        /*
         * this process here makes the Clickable Links from the text
         */
            if (linkSpec.end<=text.length()) linkableText.setSpan(linkSpec.span, linkSpec.start, linkSpec.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    /*
     * sets the text for the TextView with enabled links
     */
        setText(linkableText);
    }

    /*
     * sets the Listener for later click propagation purpose
     */
    public void setOnTextLinkClickListener(TextLinkClickListener newListener)
    {
        mListener = newListener;
    }

/*
 * The Method mainly performs the Regex Comparison for the Pattern and adds them to
 * listOfLinks array list
 */

    private final void gatherLinks(ArrayList<Hyperlink> links,
                                   Spannable s, Pattern pattern)
    {
        // Matcher matching the pattern
        Matcher m = pattern.matcher(s);

        while (m.find())
        {
            int start = m.start();
            int end = m.end();

        /*
         *  Hyperlink is basically used like a structure for storing the information about
         *  where the link was found.
         */
            Hyperlink spec = new Hyperlink();

            spec.textSpan = s.subSequence(start, end);
            spec.span = new InternalURLSpan(spec.textSpan.toString());
            spec.start = start;
            spec.end = end;

            links.add(spec);
        }
    }

    /*
     * This is class which gives us the clicks on the links which we then can use.
     */
    public class InternalURLSpan extends ClickableSpan
    {
        private String clickedSpan;

        public InternalURLSpan (String clickedString)
        {
            clickedSpan = clickedString;
        }

        @Override
        public void onClick(View textView)
        {
            mListener.onTextLinkClick(textView, clickedSpan);
        }
    }

    /*
     * Class for storing the information about the Link Location
     */
    class Hyperlink
    {
        CharSequence textSpan;
        InternalURLSpan span;
        int start;
        int end;
    }

    public interface TextLinkClickListener
    {

        /*
         *  This method is called when the TextLink is clicked from LinkEnabledTextView
         */
        public void onTextLinkClick(View textView, String clickedString);
    }

    public static class LocalLinkMovementMethod extends LinkMovementMethod {
        static LocalLinkMovementMethod sInstance;

        public static LocalLinkMovementMethod getInstance() {
            if (sInstance == null)
                sInstance = new LocalLinkMovementMethod();

            return sInstance;
        }

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer,
                                    MotionEvent event) {
            int action = event.getAction();

            if (action == MotionEvent.ACTION_UP
                    || action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                ClickableSpan[] link = buffer.getSpans(off, off,
                        ClickableSpan.class);

                if (link.length != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        link[0].onClick(widget);
                    } else if (action == MotionEvent.ACTION_DOWN) {
                        Selection.setSelection(buffer,
                                buffer.getSpanStart(link[0]),
                                buffer.getSpanEnd(link[0]));
                    }

                    if (widget instanceof EvTextView) {
                        ((EvTextView) widget).linkHit = true;
                    }
                    return true;
                } else {
                    Selection.removeSelection(buffer);
                    Touch.onTouchEvent(widget, buffer, event);
                    return false;
                }
            }
            return Touch.onTouchEvent(widget, buffer, event);
        }
    }

    public static class Utils {
        private static final String TAG = Utils.class.getSimpleName();

        private static class URLSpanNoUnderline extends URLSpan {
            public URLSpanNoUnderline(String url) {
                super(url);
            }
            @Override public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        }

        // http://stackoverflow.com/questions/4096851/remove-underline-from-links-in-textview-android
        public static void stripUnderlines(TextView textView) {
            Spannable s = new SpannableString(textView.getText());
            URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
            for (URLSpan span: spans) {
                int start = s.getSpanStart(span);
                int end = s.getSpanEnd(span);
                s.removeSpan(span);
                span = new URLSpanNoUnderline(span.getURL());
                s.setSpan(span, start, end, 0);
            }
            textView.setText(s);
        }
    }
}