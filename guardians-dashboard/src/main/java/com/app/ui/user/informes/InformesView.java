/**
* InformesView.java
* appEducacionalVaadin
* 15/1/2015 20:55:50
* Copyright David
* com.app.ui.user.informes
*/
package com.app.ui.user.informes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;

import com.app.presenter.event.EventComunication.ReportsCountUpdatedEvent;
import com.app.presenter.event.EventComunication.TransactionReportEvent;
import com.app.presenter.event.EventComunicationBus;
import com.app.ui.user.informes.InformeEditor.InformeEditorListener;
import com.app.ui.user.informes.InformeEditor.PaletteItemType;
import com.google.gwt.thirdparty.guava.common.eventbus.Subscribe;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.CloseHandler;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SpringView(name = "BanearUsuario")
/**
 * @author David
 *
 */
public class InformesView extends TabSheet implements View, CloseHandler,
InformeEditorListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 443000846275379161L;
	public static final String CONFIRM_DIALOG_ID = "confirm-dialog";

    
    @PostConstruct
	void init() {
    	setSizeFull();
        addStyleName("reports");
        addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        setCloseHandler(this);
        EventComunicationBus.register(this);

        addTab(buildDrafts());
    }

    private Component buildDrafts() {
        final VerticalLayout allDrafts = new VerticalLayout();
        allDrafts.setSizeFull();
        allDrafts.setCaption("All Drafts");

        VerticalLayout titleAndDrafts = new VerticalLayout();
        titleAndDrafts.setSizeUndefined();
        titleAndDrafts.setSpacing(true);
        titleAndDrafts.addStyleName("drafts");
        allDrafts.addComponent(titleAndDrafts);
        allDrafts
                .setComponentAlignment(titleAndDrafts, Alignment.MIDDLE_CENTER);

        Label draftsTitle = new Label("Drafts");
        draftsTitle.addStyleName(ValoTheme.LABEL_H1);
        draftsTitle.setSizeUndefined();
        titleAndDrafts.addComponent(draftsTitle);
        titleAndDrafts.setComponentAlignment(draftsTitle, Alignment.TOP_CENTER);

        titleAndDrafts.addComponent(buildDraftsList());

        return allDrafts;
    }

    private Component buildDraftsList() {
        HorizontalLayout drafts = new HorizontalLayout();
        drafts.setSpacing(true);

        drafts.addComponent(buildDraftThumb());
        drafts.addComponent(buildCreateBox());

        return drafts;
    }

    private Component buildDraftThumb() {
        VerticalLayout draftThumb = new VerticalLayout();
        draftThumb.setSpacing(true);

        draftThumb.addStyleName("draft-thumb");
        Image draft = new Image(null, new ThemeResource(
                "img/draft-report-thumb.png"));
        draft.setWidth(160.0f, Unit.PIXELS);
        draft.setHeight(200.0f, Unit.PIXELS);
        draft.setDescription("Click to edit");
        draftThumb.addComponent(draft);
        Label draftTitle = new Label(
                "Monthly revenue<br><span>Last modified 1 day ago</span>",
                ContentMode.HTML);
        draftTitle.setSizeUndefined();
        draftThumb.addComponent(draftTitle);

        final Button delete = new Button("×");
        delete.setDescription("Delete draft");
        delete.setPrimaryStyleName("delete-button");
        delete.addClickListener(new ClickListener() {
            /**
			 * 
			 */
			private static final long serialVersionUID = -4849902999806440170L;

			@Override
            public void buttonClick(final ClickEvent event) {
                Notification.show("Not implemented in this demo");
            }
        });
        draftThumb.addComponent(delete);

        draftThumb.addLayoutClickListener(new LayoutClickListener() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1330339144461131252L;

			@Override
            public void layoutClick(final LayoutClickEvent event) {
                if (event.getButton() == MouseButton.LEFT
                        && event.getChildComponent() != delete) {
                    addReport(ReportType.MONTHLY, null);
                }
            }
        });

        return draftThumb;
    }

    private Component buildCreateBox() {
        VerticalLayout createBox = new VerticalLayout();
        createBox.setWidth(160.0f, Unit.PIXELS);
        createBox.setHeight(200.0f, Unit.PIXELS);
        createBox.addStyleName("create");

        Button create = new Button("Create New");
        create.addStyleName(ValoTheme.BUTTON_PRIMARY);
        create.addClickListener(new ClickListener() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 8521282052699915983L;

			@Override
            public void buttonClick(final ClickEvent event) {
                addReport(ReportType.EMPTY, null);
            }
        });

        createBox.addComponent(create);
        createBox.setComponentAlignment(create, Alignment.MIDDLE_CENTER);
        return createBox;
    }

    public void addReport(final ReportType reportType, final Object prefillData) {
        InformeEditor reportEditor = new InformeEditor(this);
        addTab(reportEditor).setClosable(true);

        if (reportType == ReportType.MONTHLY) {
            reportEditor.setTitle("Monthly revenue");
            reportEditor.addWidget(PaletteItemType.CHART, null);
            reportEditor.addWidget(PaletteItemType.TABLE, null);
        } else if (reportType == ReportType.EMPTY) {
            DateFormat df = new SimpleDateFormat("M/dd/yyyy");
            reportEditor.setTitle("Unnamed Report – " + (df.format(new Date()))
                    + " (" + getComponentCount() + ")");
        } else if (reportType == ReportType.TRANSACTIONS) {
            reportEditor
                    .setTitle("Generated report from selected transactions");
            reportEditor.addWidget(PaletteItemType.TEXT, "");
            reportEditor.addWidget(PaletteItemType.TRANSACTIONS, prefillData);
        }

        EventComunicationBus.post(new ReportsCountUpdatedEvent(
                getComponentCount() - 1));
        setSelectedTab(getComponentCount() - 1);
    }

    @Subscribe
    public void createTransactionReport(final TransactionReportEvent event) {
        addReport(ReportType.TRANSACTIONS, event.getTransactions());
    }

    @Override
    public void onTabClose(final TabSheet tabsheet, final Component tabContent) {
        Label message = new Label(
                "You have not saved this report. Do you want to save or discard any changes you've made to this report?");
        message.setWidth("25em");

        final Window confirmDialog = new Window("Unsaved Changes");
        confirmDialog.setId(CONFIRM_DIALOG_ID);
        confirmDialog.setCloseShortcut(KeyCode.ESCAPE, null);
        confirmDialog.setModal(true);
        confirmDialog.setClosable(false);
        confirmDialog.setResizable(false);

        VerticalLayout root = new VerticalLayout();
        root.setSpacing(true);
        root.setMargin(true);
        confirmDialog.setContent(root);

        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth("100%");
        footer.setSpacing(true);

        root.addComponents(message, footer);

        Button ok = new Button("Save", new ClickListener() {
            /**
			 * 
			 */
			private static final long serialVersionUID = -4077225046134688039L;

			@Override
            public void buttonClick(final ClickEvent event) {
                confirmDialog.close();
                removeComponent(tabContent);
                EventComunicationBus.post(new ReportsCountUpdatedEvent(
                        getComponentCount() - 1));
                Notification
                        .show("The report was saved as a draft",
                                "Actually, the report was just closed and deleted forever. As this is only a demo, it doesn't persist any data.",
                                Type.TRAY_NOTIFICATION);
            }
        });
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);

        Button discard = new Button("Discard Changes", new ClickListener() {
            /**
			 * 
			 */
			private static final long serialVersionUID = -4376719642251378061L;

			@Override
            public void buttonClick(final ClickEvent event) {
                confirmDialog.close();
                removeComponent(tabContent);
                EventComunicationBus.post(new ReportsCountUpdatedEvent(
                        getComponentCount() - 1));
            }
        });
        discard.addStyleName(ValoTheme.BUTTON_DANGER);

        Button cancel = new Button("Cancel", new ClickListener() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 8630589828797421419L;

			@Override
            public void buttonClick(final ClickEvent event) {
                confirmDialog.close();
            }
        });

        footer.addComponents(discard, cancel, ok);
        footer.setExpandRatio(discard, 1);

        getUI().addWindow(confirmDialog);
        confirmDialog.focus();
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

    @Override
    public void titleChanged(final String newTitle, final InformeEditor editor) {
        getTab(editor).setCaption(newTitle);
    }

    public enum ReportType {
        MONTHLY, EMPTY, TRANSACTIONS
    }
}
