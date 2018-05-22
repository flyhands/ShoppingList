package android.vogella.de.shoppinglist;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

public class MainActivity extends AppCompatActivity {

    private SectionPageAdapter mSectionPageAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());


        // Setup the ViewPager with the sections adapter
        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }

    private void setupViewPager(ViewPager viewPager) {

        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new ShoppingList(), "Shopping List");
        adapter.addFragment(new CartFragment(), "Cart");

        viewPager.setAdapter(adapter);


    }

    public ViewPager getViewPager(){
        return mViewPager;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
