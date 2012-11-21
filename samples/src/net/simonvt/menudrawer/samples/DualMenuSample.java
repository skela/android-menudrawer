package net.simonvt.menudrawer.samples;

import net.simonvt.menudrawer.R;
import net.simonvt.widget.MenuDrawer;
import net.simonvt.widget.MenuDrawerManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;

public class DualMenuSample extends Activity implements View.OnClickListener
{
	private static final String STATE_MENUDRAWER = "net.simonvt.menudrawer.samples.WindowSample.menuDrawer";
	private static final String STATE_ACTIVE_VIEW_ID = "net.simonvt.menudrawer.samples.WindowSample.activeViewId";

	private static final int MENU_OVERFLOW = 1;
	
	private MenuDrawerManager mMenuDrawer;
	private TextView mContentTextView;
	
	private int mActiveViewId;

	@SuppressLint({ "NewApi", "NewApi" })
	@Override
	public void onCreate(Bundle inState)
	{
		super.onCreate(inState);
		if (inState != null)
		{
			mActiveViewId = inState.getInt(STATE_ACTIVE_VIEW_ID);
		}
		
		mMenuDrawer = new MenuDrawerManager(this, MenuDrawer.MENU_DRAG_WINDOW,MenuDrawer.MENU_POSITION_BOTH);
		mMenuDrawer.setContentView(R.layout.activity_windowsample);
		mMenuDrawer.setMenuView(R.layout.menu_scrollview);
 
		MenuScrollView msv = (MenuScrollView) mMenuDrawer.getMenuView();
		msv.setOnScrollChangedListener(new MenuScrollView.OnScrollChangedListener()
		{
			@Override
			public void onScrollChanged()
			{
				mMenuDrawer.getMenuDrawer().invalidate();
			}
		});

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		{
			getActionBar().setDisplayHomeAsUpEnabled(true);			
		}

		mContentTextView = (TextView) findViewById(R.id.contentText);
		
		TextView activeView = (TextView) findViewById(mActiveViewId);
		if (activeView != null)
		{
			mMenuDrawer.setActiveView(activeView);
			mContentTextView.setText("Active item: " + activeView.getText());
		}

	}

	boolean flipper;
	
	public void clickedLeftRight()
	{		
		flipper = ! flipper;
		
		if (flipper)
			clickedRight();
		else
			clickedLeft();
	}
	
	public void clickedRight()
	{
		mMenuDrawer.setMenuPosition(MenuDrawer.MENU_POSITION_RIGHT);
		mMenuDrawer.setMenuView(R.layout.menu_scrollview);
		mMenuDrawer.toggleMenu();
	}
	
	public void clickedLeft()
	{
		mMenuDrawer.setMenuPosition(MenuDrawer.MENU_POSITION_LEFT);
		mMenuDrawer.setMenuView(R.layout.menu_scrollview2);
		mMenuDrawer.toggleMenu();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();		
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle inState)
	{
		super.onRestoreInstanceState(inState);
		mMenuDrawer.onRestoreDrawerState(inState
				.getParcelable(STATE_MENUDRAWER));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putParcelable(STATE_MENUDRAWER,
				mMenuDrawer.onSaveDrawerState());
		outState.putInt(STATE_ACTIVE_VIEW_ID, mActiveViewId);
	}

	@SuppressLint("NewApi")
	@Override
    public boolean onCreateOptionsMenu(Menu menu) 
	{
        MenuItem overflowItem = menu.add(0, MENU_OVERFLOW, 0, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            overflowItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        overflowItem.setIcon(R.drawable.ic_menu_moreoverflow_normal_holo_light);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
			{
				clickedLeft();
				return true;
			}			
			case MENU_OVERFLOW:
			{
				clickedRight();                
                return true;
			}
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed()
	{
		final int drawerState = mMenuDrawer.getDrawerState();
		if (drawerState == MenuDrawer.STATE_OPEN
				|| drawerState == MenuDrawer.STATE_OPENING)
		{
			mMenuDrawer.closeMenu();
			return;
		}

		super.onBackPressed();
	}

	@Override
	public void onClick(View v)
	{
		mMenuDrawer.setActiveView(v);
		mContentTextView.setText("Active item: " + ((TextView) v).getText());
		mMenuDrawer.closeMenu();
		mActiveViewId = v.getId();
	}
}
