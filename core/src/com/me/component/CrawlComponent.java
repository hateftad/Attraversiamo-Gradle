package com.me.component;

public class CrawlComponent extends BaseComponent {

	public boolean canCrawl;
	public boolean isCrawling;
	public boolean isStanding;
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		canCrawl = false;
		isCrawling = false;
		isStanding = false;
	}

}
