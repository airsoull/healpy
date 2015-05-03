package cl.healpy.config;

import android.content.Intent;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.heinrichreimersoftware.materialdrawer.DrawerActivity;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;

import cl.healpy.ComunaActivity;
import cl.healpy.MainActivity;
import cl.healpy.R;

public class ConfigHealpy {

    private Toolbar toolbar;
    private DrawerActivity activity;

    public ConfigHealpy(){}

    public ConfigHealpy(DrawerActivity activity){
        this.setActivity(activity);
    }

    private void setActivity(DrawerActivity activity){
        this.activity = activity;
    }

    private DrawerActivity getActivity(){
        return this.activity;
    }

    public void setDrawer(){

        // Si estas en búsqueda de comuna solo se mostrará menú para mapa
        if (getActivity().getClass() == ComunaActivity.class){}
        getActivity().addItem(new DrawerItem()
                        .setId(1)
                        .setImage(activity.getResources().getDrawable(R.mipmap.ic_map_grey600_36dp))
                        .setTextPrimary(activity.getString(R.string.show_map))
        );

        //Si estas en mapa solo se mostrará menú para mostrar comuna
        getActivity().addItem(new DrawerItem()
                        .setId(2)
                        .setImage(activity.getResources().getDrawable(R.mipmap.ic_search_grey600_36dp))
                        .setTextPrimary(activity.getString(R.string.show_comuna))
        );

        // getActivity().addDivider();


        getActivity().setOnItemClickListener(new DrawerItem.OnItemClickListener() {
            @Override
            public void onClick(DrawerItem item, long id, int position) {
                if (item.getId() == 2) {
                    openComuna(getActivity());
                }
                if (item.getId() == 1) {
                    openMap(getActivity());
                }
            }
        });
    }

    private void openComuna(DrawerActivity activity){
        Intent intent = new Intent(activity, ComunaActivity.class);
        activity.startActivity(intent);
    }

    private void openMap(DrawerActivity activity){
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    public void setToolbar(String text){
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(text);
        getActivity().setSupportActionBar(toolbar);
    }

    public String prettyfyJSON(String json){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser parser = new JsonParser();

        JsonElement element = parser.parse(json);
        return gson.toJson(element);
    }

}
