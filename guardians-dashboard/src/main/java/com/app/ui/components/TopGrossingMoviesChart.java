/**
 * TopGrossingMoviesChart.java
 * appEducacionalVaadin
 * 18/1/2015 21:32:27
 * Copyright David
 * com.app.ui.components
 */
package com.app.ui.components;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.app.applicationservices.services.ProfesorService;
import com.app.domain.model.types.ItemEvaluable;
import com.app.domain.model.types.Profesor;
import com.app.infrastructure.security.Authority;
import com.app.infrastructure.security.UserAccount;
import com.app.ui.AppUI;
import com.google.common.collect.Lists;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Credits;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsBar;
import com.vaadin.addon.charts.model.Series;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.UI;

@ViewScope
@SpringComponent
/**
 * @author David
 *
 */
public class TopGrossingMoviesChart extends Chart {


	@Autowired
	private ProfesorService profesorService;

	/**
	 * 
	 */
	private static final long serialVersionUID = -1387352152513936704L;

	public TopGrossingMoviesChart() {
		

	}

	/* (non-Javadoc)
	 * @see com.vaadin.ui.AbstractComponent#attach()
	 */
	@Override
	public void attach() {
		super.attach();
		setCaption("Top Grossing Movies");
		getConfiguration().setTitle("");
		getConfiguration().getChart().setType(ChartType.BAR);
		getConfiguration().getChart().setAnimation(false);
		getConfiguration().getxAxis().getLabels().setEnabled(false);
		getConfiguration().getxAxis().setTickWidth(0);
		getConfiguration().getyAxis().setTitle("");
		setSizeFull();
		 UserAccount ua = AppUI.getCurrentUser();
		List<ItemEvaluable> movies = new ArrayList<ItemEvaluable>(profesorService.findAllItems(profesorService.findByUserAccount(ua)));

		List<Series> series = new ArrayList<Series>();
		if (movies.size() >= 5){
				
			
			for (int i = 0; i < 6; i++) {
				ItemEvaluable itemEvaluable = movies.get(i);
				PlotOptionsBar opts = new PlotOptionsBar();
				// opts.setColor(Color.RED);
				opts.setBorderWidth(0);
				opts.setShadow(false);
				opts.setPointPadding(0.4);
				opts.setAnimation(false);
				ListSeries item = new ListSeries(itemEvaluable.getTitulo(),
						itemEvaluable.getCalificacion());
				item.setPlotOptions(opts);
				series.add(item);
	
			}
		}
		getConfiguration().setSeries(series);

		Credits c = new Credits("");
		getConfiguration().setCredits(c);

		PlotOptionsBar opts = new PlotOptionsBar();
		opts.setGroupPadding(0);
		getConfiguration().setPlotOptions(opts);
	}
	
	

}
