/**
 * Stub/model code simplified/modified from the Android library
 */

package android.text;

public class SpannableStringBuilder implements CharSequence, Editable,
        Appendable {

    public SpannableStringBuilder() {
        this("");
    }

    public SpannableStringBuilder(CharSequence text) {
        this(text == null ? " " : text, 0, text == null ? 1 : text.length());
    }

    public SpannableStringBuilder(CharSequence text, int start, int end) {
        int srclen = end - start;

        if (srclen < 0) throw new StringIndexOutOfBoundsException();

        int len = srclen + 1;
        mText = new char[len];

        mText = text.toString().toCharArray();

        // reduce logged field accesses
        /*if(srclen > 0) {
            for (int i = 0; i < srclen; i++) {
                mText[i] = text.charAt(i);
            }
        }*/
    }

    public static SpannableStringBuilder valueOf(CharSequence source) {
        if (source instanceof SpannableStringBuilder) {
            return (SpannableStringBuilder) source;
        } else {
            return new SpannableStringBuilder(source);
        }
    }

    public char charAt(int where) {
        int len = length();
        if (where < 0) {
            throw new IndexOutOfBoundsException("charAt: " + where + " < 0");
        } else if (where >= len) {
            throw new IndexOutOfBoundsException("charAt: " + where + " >= length " + len);
        }

        if (where >= mGapStart)
            return mText[where + mGapLength];
        else
            return mText[where];
    }

    public int length() {
        return mText.length - mGapLength;
    }

    private void resizeFor(int size) {
        final int oldLength = mText.length;
        final int newLength = size + 1;
        final int delta = newLength - oldLength;
        if (delta == 0) return;

        char[] newText = new char[newLength];
        System.arraycopy(mText, 0, newText, 0, mGapStart);
        final int after = oldLength - (mGapStart + mGapLength);
        System.arraycopy(mText, oldLength - after, newText, newLength - after, after);
        mText = newText;

        mGapLength += delta;
        if (mGapLength < 1)
            new Exception("mGapLength < 1").printStackTrace();

        for (int i = 0; i < mSpanCount; i++) {
            if (mSpanStarts[i] > mGapStart) mSpanStarts[i] += delta;
            if (mSpanEnds[i] > mGapStart) mSpanEnds[i] += delta;
        }
    }

    private void moveGapTo(int where) {
        if (where == mGapStart)
            return;

        boolean atEnd = (where == length());

        if (where < mGapStart) {
            int overlap = mGapStart - where;
            System.arraycopy(mText, where, mText, mGapStart + mGapLength - overlap, overlap);
        } else /* where > mGapStart */ {
            int overlap = where - mGapStart;
            System.arraycopy(mText, where + mGapLength - overlap, mText, mGapStart, overlap);
        }

        // XXX be more clever
        for (int i = 0; i < mSpanCount; i++) {
            int start = mSpanStarts[i];
            int end = mSpanEnds[i];

            if (start > mGapStart)
                start -= mGapLength;
            if (start > where)
                start += mGapLength;
            else if (start == where) {
                int flag = (mSpanFlags[i] & START_MASK) >> START_SHIFT;

                if (flag == POINT || (atEnd && flag == PARAGRAPH))
                    start += mGapLength;
            }

            if (end > mGapStart)
                end -= mGapLength;
            if (end > where)
                end += mGapLength;
            else if (end == where) {
                int flag = (mSpanFlags[i] & END_MASK);

                if (flag == POINT || (atEnd && flag == PARAGRAPH))
                    end += mGapLength;
            }

            mSpanStarts[i] = start;
            mSpanEnds[i] = end;
        }

        mGapStart = where;
    }

    // Documentation from interface
    public SpannableStringBuilder insert(int where, CharSequence tb, int start, int end) {
        return replace(where, where, tb, start, end);
    }

    // Documentation from interface
    public SpannableStringBuilder insert(int where, CharSequence tb) {
        return replace(where, where, tb, 0, tb.length());
    }

    // Documentation from interface
    public SpannableStringBuilder delete(int start, int end) {
        SpannableStringBuilder ret = replace(start, end, "", 0, 0);

        if (mGapLength > 2 * length())
            resizeFor(length());

        return ret; // == this
    }

    // Documentation from interface
    public void clear() {
        replace(0, length(), "", 0, 0);
    }

    // Documentation from interface
    public void clearSpans() {
        for (int i = mSpanCount - 1; i >= 0; i--) {
            Object what = mSpans[i];
            int ostart = mSpanStarts[i];
            int oend = mSpanEnds[i];

            if (ostart > mGapStart)
                ostart -= mGapLength;
            if (oend > mGapStart)
                oend -= mGapLength;

            mSpanCount = i;
            mSpans[i] = null;

            sendSpanRemoved(what, ostart, oend);
        }
    }

    // Documentation from interface
    public SpannableStringBuilder append(CharSequence text) {
        int length = length();
        return replace(length, length, text, 0, text.length());
    }

    // Documentation from interface
    public SpannableStringBuilder append(CharSequence text, int start, int end) {
        int length = length();
        return replace(length, length, text, start, end);
    }

    // Documentation from interface
    public SpannableStringBuilder append(char text) {
        return append(String.valueOf(text));
    }

    private void change(int start, int end, CharSequence cs, int csStart, int csEnd) {
    }

    private int updatedIntervalBound(int offset, int start, int nbNewChars, int flag, boolean atEnd,
                                     boolean textIsRemoved) {
        return -1;
    }

    private void removeSpan(int i) {
    }

    // Documentation from interface
    public SpannableStringBuilder replace(int start, int end, CharSequence tb) {
        return replace(start, end, tb, 0, tb.length());
    }

    // Documentation from interface
    public SpannableStringBuilder replace(final int start, final int end,
                                          CharSequence tb, int tbstart, int tbend) {
        return this;
    }

    private static boolean hasNonExclusiveExclusiveSpanAt(CharSequence text, int offset) {
        return false;
    }

    private void sendToSpanWatchers(int replaceStart, int replaceEnd, int nbNewChars) {
    }

    public void setSpan(Object what, int start, int end, int flags) {
        setSpan(true, what, start, end, flags);
    }

    private void setSpan(boolean send, Object what, int start, int end, int flags) {
    }

    public int getSpanStart(Object what) {
        return -1;
    }

    public int getSpanEnd(Object what) {
        return -1;
    }

    public int getSpanFlags(Object what) {
        return 0;
    }

    /*
    public <T> T[] getSpans(int queryStart, int queryEnd, Class<T> kind) {
    }*/

    public int nextSpanTransition(int start, int limit, Class kind) {
        return -1;
    }

    public CharSequence subSequence(int start, int end) {
        return new SpannableStringBuilder(this, start, end);
    }

    public void getChars(int start, int end, char[] dest, int destoff) {
    }

    @Override
    public String toString() {
        int len = length();
        char[] buf = new char[len];

        getChars(0, len, buf, 0);
        return new String(buf);
    }

    public String substring(int start, int end) {
        char[] buf = new char[end - start];
        getChars(start, end, buf, 0);
        return new String(buf);
    }

    private void sendSpanRemoved(Object what, int start, int end) {
    }

    private static String region(int start, int end) {
        return "(" + start + " ... " + end + ")";
    }

    private void checkRange(final String operation, int start, int end) {
    }


    private char[] mText;
    private int mGapStart;
    private int mGapLength;

    private Object[] mSpans;
    private int[] mSpanStarts;
    private int[] mSpanEnds;
    private int[] mSpanFlags;
    private int mSpanCount;
    private int mSpanCountBeforeAdd;

    private static final int MARK = 1;
    private static final int POINT = 2;
    private static final int PARAGRAPH = 3;

    private static final int START_MASK = 0xF0;
    private static final int END_MASK = 0x0F;
    private static final int START_SHIFT = 4;
}
