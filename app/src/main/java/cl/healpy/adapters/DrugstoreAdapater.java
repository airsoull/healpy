package cl.healpy.adapters;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cl.healpy.R;

public class DrugstoreAdapater extends CursorAdapter {

    private Context mContext;

    public DrugstoreAdapater(Context context, Cursor cursor){
        super(context, cursor, 0);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.drugstore_list, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView text_name = (TextView) view.findViewById(R.id.text_name);
        TextView text_address = (TextView) view.findViewById(R.id.text_address);
        TextView text_phone = (TextView) view.findViewById(R.id.text_phone);
        TextView text_schedule_start = (TextView) view.findViewById(R.id.text_schedule_start);
        TextView text_schedule_end = (TextView) view.findViewById(R.id.text_schedule_end);
        TextView text_town_name = (TextView) view.findViewById(R.id.text_town_name);
        ImageView imageView_drugstore = (ImageView) view.findViewById(R.id.imageView);

        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
        String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
        String opening_time = cursor.getString(cursor.getColumnIndexOrThrow("opening_time"));
        String closing_time = cursor.getString(cursor.getColumnIndexOrThrow("closing_time"));
        String town_name = cursor.getString(cursor.getColumnIndexOrThrow("town_name"));
        int all_night = cursor.getInt(cursor.getColumnIndexOrThrow("all_night"));

        text_name.setText(name);
        text_address.setText(address);
        text_phone.setText(phone);
        text_schedule_start.setText(opening_time);
        text_schedule_end.setText(closing_time);
        text_town_name.setText(town_name);

        if (all_night == 1){
            imageView_drugstore.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_local_convenience_store_grey600_36dp));
        } else {
            imageView_drugstore.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_store_grey600_36dp));
        }
    }
}
