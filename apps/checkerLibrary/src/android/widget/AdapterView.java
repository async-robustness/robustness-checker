package android.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * This class is only used as a parameter type for some overriden methods
 */
public class AdapterView<T/* extends Adapter*/> extends ViewGroup {

  public static final int INVALID_POSITION = -1;

  private OnItemClickListener mOnItemClickListener;
  private OnItemSelectedListener mOnItemSelectedListener;
  private OnItemLongClickListener mOnItemLongClickListener;

  public AdapterView() {

  }

  public AdapterView(Context context) {

  }

  public Object getItemAtPosition(int position, Class itemClass) {
    Object item = null;

    try{
      item = itemClass.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    return item;
  }

  public int getSelectedItemPosition() {
    return 0;
  }

  public interface OnItemClickListener {

    void onItemClick(AdapterView<?> parent, View view, int position, long id);
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    mOnItemClickListener = listener;
  }

  public boolean callOnItemClickListener(int pos, int id) {
    if(mOnItemClickListener == null)
      return false;

    mOnItemClickListener.onItemClick(new ListView(), new View(), pos, id);
    return true;
  }

  public final OnItemClickListener getOnItemClickListener() {
    return mOnItemClickListener;
  }

  public interface OnItemSelectedListener {
    void onItemSelected(AdapterView<?> parent, View view, int position, long id);
    void onNothingSelected(AdapterView<?> parent);
  }

  public void setOnItemSelectedListener(OnItemSelectedListener listener) {
    mOnItemSelectedListener = listener;
  }

  public boolean callOnItemSelectedListener(int pos, int id) {
    if(mOnItemSelectedListener == null)
      return false;

    mOnItemSelectedListener.onItemSelected(new ListView(), new View(), pos, id);
    return true;
  }

  public final OnItemSelectedListener getOnItemSelectedListener() {
    return mOnItemSelectedListener;
  }

  public static class AdapterContextMenuInfo /*implements ContextMenu.ContextMenuInfo*/ {

    public AdapterContextMenuInfo(View targetView, int position, long id) {
      this.targetView = targetView;
      this.position = position;
      this.id = id;
    }
    public View targetView;
    public int position;
    public long id;
  }

  public interface OnItemLongClickListener {

    boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id);
  }

  public void setOnItemLongClickListener(OnItemLongClickListener listener) {
    mOnItemLongClickListener = listener;
  }
}
