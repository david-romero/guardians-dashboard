/**
* TopSixTheatersChart.java
* appEducacionalVaadin
* 18/1/2015 21:40:55
* Copyright David
* com.app.ui.components
*/
package com.app.ui.components;

import java.util.ArrayList;
import java.util.List;

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
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.PlotOptionsPie;
import com.vaadin.ui.UI;

/**
 * @author David
 *
 */
public class TopSixTheatersChart extends Chart{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6409520586659190071L;

	private transient ApplicationContext applicationContext;

	private ProfesorService profesorService;

	public TopSixTheatersChart() {
        super(ChartType.PIE);
		loadBeans();
        setCaption("Popular Movies");
        getConfiguration().setTitle("");
        getConfiguration().getChart().setType(ChartType.PIE);
        getConfiguration().getChart().setAnimation(false);
        setWidth("100%");
        setHeight("90%");

        DataSeries series = new DataSeries();

        UserAccount account = AppUI.getCurrentUser();
    	Profesor profesor = null;
    	switch ( Lists.newArrayList(account.getAuthorities()).get(0).getAuthority()) {
		case Authority.PROFESOR:
			profesor = profesorService.findByUserAccount(account);
			break;
		case Authority.TUTOR:
			break;

		default:
			break;
		}
		List<ItemEvaluable> movies = new ArrayList<ItemEvaluable>(profesorService.findAllItems(profesor));
        for (int i = 0; i < 6; i++) {
            ItemEvaluable movie = movies.get(i);
            DataSeriesItem item = new DataSeriesItem(movie.getTitulo(),
                    movie.getCalificacion());
            series.add(item);
            //item.setColor(DummyDataGenerator.chartColors[5 - i]);
        }
        getConfiguration().setSeries(series);

        PlotOptionsPie opts = new PlotOptionsPie();
        opts.setBorderWidth(0);
        opts.setShadow(false);
        opts.setAnimation(false);
        getConfiguration().setPlotOptions(opts);

        Credits c = new Credits("");
        getConfiguration().setCredits(c);
    }
	
	/**
	 * @author David
	 */
	private void loadBeans() {
		applicationContext = ( (AppUI) UI.getCurrent() ).getApplicationContext();
		profesorService = applicationContext.getBean(ProfesorService.class);
	}

	
}
