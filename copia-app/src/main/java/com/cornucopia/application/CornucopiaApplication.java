package com.cornucopia.application;

import android.content.Context;

import androidx.multidex.MultiDexApplication;
import androidx.room.Room;

import com.bugsnag.android.Bugsnag;
import com.cornucopia.BuildConfig;
import com.cornucopia.aspect.dexposed.DexposedHook;
import com.cornucopia.di.dagger2.D2GraphComponent;
import com.cornucopia.hotfix.Hotfix;
import com.cornucopia.jetpack.data.persist.UserDatabase;
import com.cornucopia.storage.ticketsmanager.Tickets;
import com.cornucopia.storage.ticketsmanager.TicketsSQLiteOpenHelper;
import com.facebook.stetho.Stetho;
import com.google.android.play.core.splitcompat.SplitCompatApplication;

import java.util.ArrayList;

import io.flowup.FlowUp;


public class CornucopiaApplication extends SplitCompatApplication {

	private ArrayList<Tickets> currentTickets;

	private TicketsSQLiteOpenHelper ticketDBHelper;
	
	private static D2GraphComponent graph;
	
//	private static RefWatcher refWatcher;
	
	private static CornucopiaApplication instance;
	
	@Override
	protected void attachBaseContext(Context base) {
	    super.attachBaseContext(base);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		instance = this;
		
	    // 异常捕获 
        initCrashHandler();
		
		// 数据库操作
		ticketDBHelper = new TicketsSQLiteOpenHelper(this);
		
		if (null == currentTickets) {
//			currentTickets = new ArrayList<Tickets>();
			
			// 使用数据库
			currentTickets = ticketDBHelper.loadTickets();
		}
		
		Hotfix hotfix = new Hotfix();
		hotfix.loadBugfix(this, "patch_dex.jar", "com.cornucopia.hotfix.HotfixBug");
		
		DexposedHook dexposed = new DexposedHook();
		dexposed.hook(this);
		
		buildComponentGraph();
		
//		initRealmInstance();
		
		// no found class
//		AppBlockCanaryContext appBlock = new AppBlockCanaryContext(); 
//		BlockCanary.install(this, appBlock).start();

		// room database
		UserDatabase db = Room.databaseBuilder(getApplicationContext(),
				UserDatabase.class, "user-db").build();

		Stetho.initializeWithDefaults(this);

		FlowUp.Builder.with(this)
				.apiKey("a090c1b35c024aeda4c4c64d6909f0b4")
				.forceReports(BuildConfig.DEBUG)
				.start();

	}

    private void initCrashHandler() {
        Bugsnag.start(this);
        
//        MetaData metaData = new MetaData();
//        metaData.addToTab("User", "username", "thom");
//        Bugsnag.notify(new Exception("Non-fatal"), Severity.INFO,  metaData);
	}

	// 设置和获取当前的ticket
	public ArrayList<Tickets> getCurrentTickets() {
		return currentTickets;
	}

	public void setCurrentTickets(ArrayList<Tickets> currentTicket) {
		this.currentTickets = currentTicket;
	}
	
	// 添加ticket
	public void addTicket(Tickets ticket) {
		//
		assert(null != ticket);
		
		// 使用对象前最好对对象进行非空判断
		if (null == currentTickets) {
			currentTickets = new ArrayList<Tickets>();
		}
		
		currentTickets.add(ticket);
	}

	public TicketsSQLiteOpenHelper getTicketDBHelper() {
		return ticketDBHelper;
	}

	
    private void buildComponentGraph() {
        graph = D2GraphComponent.Initializer.init(instance);
    }
    
    public static D2GraphComponent component() {
        return graph;
    }
    
//    private void initRealmInstance() {
//        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
//        Realm.setDefaultConfiguration(config);
//    }

}
