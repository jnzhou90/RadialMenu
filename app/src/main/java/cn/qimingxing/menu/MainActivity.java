package cn.qimingxing.menu;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.radialmenu.OnRadailMenuClick;
import com.example.radialmenu.RadialMenuItem;
import com.example.radialmenu.RadialMenuRenderer;

import java.util.ArrayList;



public class MainActivity extends FragmentActivity {

    //Variable declarations
    private RadialMenuRenderer mRenderer;
    private FrameLayout mHolderLayout;
    public RadialMenuItem menuContactItem, menuMainItem, menuAboutItem;
    private ArrayList<RadialMenuItem> mMenuItems = new ArrayList<>(9);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_holder);

        //Init the frame layout
        mHolderLayout = findViewById(R.id.fragment_container);
        // Init the Radial Menu and menu items
        mRenderer = new RadialMenuRenderer(mHolderLayout, false, 100, 500);
        menuContactItem = new RadialMenuItem(getResources().getString(R.string.contact),getResources().getString(R.string.contact));
        menuMainItem = new RadialMenuItem(getResources().getString(R.string.main_menu), getResources().getString(R.string.main_menu));
        menuAboutItem = new RadialMenuItem(getResources().getString(R.string.about), getResources().getString(R.string.about));
        //Add the menu Items
        mMenuItems.add(menuMainItem);
        mMenuItems.add(menuAboutItem);
        mMenuItems.add(menuContactItem);
        mMenuItems.add(menuContactItem);
        mMenuItems.add(menuMainItem);
        mMenuItems.add(menuAboutItem);
        mMenuItems.add(menuContactItem);
        mMenuItems.add(menuContactItem);
        
        mRenderer.setRadialMenuContent(mMenuItems);
        mHolderLayout.addView(mRenderer.renderView());
        //Handle the menu item interactions
        menuAboutItem.setOnRadialMenuClickListener(new OnRadailMenuClick() {
            @Override
            public void onRadailMenuClickedListener(String id) {
                Toast.makeText(MainActivity.this, "down", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRadailMenuActionDownListener(String name) {
                Toast.makeText(MainActivity.this, " " + name, Toast.LENGTH_SHORT).show();
            }
        });
        menuMainItem.setOnRadialMenuClickListener(new OnRadailMenuClick() {
            @Override
            public void onRadailMenuClickedListener(String id) {
                Toast.makeText(MainActivity.this, "down", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRadailMenuActionDownListener(String name) {
                Toast.makeText(MainActivity.this, " " + name, Toast.LENGTH_SHORT).show();

            }
        });
        menuContactItem.setOnRadialMenuClickListener(new OnRadailMenuClick() {
            @Override
            public void onRadailMenuClickedListener(String id) {
                Toast.makeText(MainActivity.this, "down", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRadailMenuActionDownListener(String name) {
                Toast.makeText(MainActivity.this, " " + name, Toast.LENGTH_SHORT).show();

            }
        });

    }


}
