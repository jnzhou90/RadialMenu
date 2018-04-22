package cn.qimingxing.menu;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.radialmenu.OnRadailMenuClick;
import com.example.radialmenu.RadialMenuItem;
import com.example.radialmenu.RadialMenuRenderer;
import com.example.radialmenu.RadialMenuView;

import java.util.ArrayList;



public class MainActivity extends FragmentActivity {

    //Variable declarations
    private RadialMenuRenderer mRenderer;
    private FrameLayout mHolderLayout;
    private ArrayList<RadialMenuItem> mMenuItems = new ArrayList<>(9);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_holder);


        //Init the frame layout
        mHolderLayout = findViewById(R.id.fragment_container);
        // Init the Radial Menu and menu items
        mRenderer = new RadialMenuRenderer(mHolderLayout, true, 100, 350, false);
        mMenuItems.add(new RadialMenuItem("1", "1",  R.drawable.ic_launcher));
        mMenuItems.add(new RadialMenuItem("2", "2",  R.drawable.ic_launcher));
        mMenuItems.add(new RadialMenuItem("3", "3",  R.drawable.ic_launcher));
        mMenuItems.add(new RadialMenuItem("4", "4",  R.drawable.ic_launcher));
        mMenuItems.add(new RadialMenuItem("5", "5",  R.drawable.ic_launcher));
//        mMenuItems.add(new RadialMenuItem("6", "6",  R.drawable.ic_launcher));
//        mMenuItems.add(new RadialMenuItem("7", "7",  R.drawable.ic_launcher));
//        mMenuItems.add(new RadialMenuItem("8", "8",  R.drawable.ic_launcher));
//        mMenuItems.add(new RadialMenuItem("9", "9",  R.drawable.ic_launcher));
//        mMenuItems.add(new RadialMenuItem("0", "0",  R.drawable.ic_launcher));

        mRenderer.setRadialMenuContent(mMenuItems);
        RadialMenuView view = mRenderer.renderView();
        mHolderLayout.addView(view);
        //Handle the menu item interactions
        view.setOnRadialMenuClickListener(new OnRadailMenuClick() {
            @Override
            public void onRadailMenuClickedListener(String id) {
                switch (id) {
                    case "1":
                        Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT).show();
                        break;
                    case "2":
                        Toast.makeText(MainActivity.this, "2", Toast.LENGTH_SHORT).show();

                        break;
                    case "3":
                        break;
                    case "4":
                        break;
                    case "5":
                        break;
                    case "6":
                        break;
                    case "7":
                        break;
                    case "8":
                        break;
                    case "9":
                        break;
                    case "0":
                        break;
                    case "保存":
                        break;
                    case "退出":
                        break;

                }
            }

            @Override
            public void onRadailMenuActionDownListener(String id) {

            }
        });


    }


}
