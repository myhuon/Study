package ddwucom.mobile.hw02;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MyCursorAdapter extends CursorAdapter {
    LayoutInflater inflater;
    int layout;

    public MyCursorAdapter(Context context, int layout, Cursor c) {
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(layout, parent, false);
        ViewHolder holder = new ViewHolder();
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder)view.getTag();

        if(holder.tvAreaName == null){
            holder.tvAreaName = view.findViewById(R.id.tvAreaName);
            holder.tvAreaPhone = view.findViewById(R.id.tvAreaPhone);
            holder.tvAreaAddress = view.findViewById(R.id.tvAreaAddress);
        }

        holder.tvAreaName.setText(cursor.getString(cursor.getColumnIndex(AreaDBHelper.COL_NAME)));
        holder.tvAreaPhone.setText(cursor.getString(cursor.getColumnIndex(AreaDBHelper.COL_PHONE)));
        holder.tvAreaAddress.setText(cursor.getString(cursor.getColumnIndex(AreaDBHelper.COL_ADDRESS)));
    }

    static class ViewHolder {

        public ViewHolder(){
            tvAreaName = null;
            tvAreaPhone = null;
            tvAreaAddress = null;
        }

        TextView tvAreaName;
        TextView tvAreaPhone;
        TextView tvAreaAddress;
    }
}
