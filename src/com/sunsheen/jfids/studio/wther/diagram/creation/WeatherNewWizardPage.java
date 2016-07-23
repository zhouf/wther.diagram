package com.sunsheen.jfids.studio.wther.diagram.creation;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import com.sunsheen.jfids.studio.dialog.image.DialogImageProvider;
import com.sunsheen.jfids.studio.run.ui.RunMessages;
import com.sunsheen.jfids.studio.run.ui.dialog.viewer.DPackage;
import com.sunsheen.jfids.studio.run.ui.utils.SelectionDialogUtils;
import com.sunsheen.jfids.studio.run.ui.validation.FilenameValidationUtility;
import com.sunsheen.jfids.studio.run.ui.widgets.FormLayoutFactory;
import com.sunsheen.jfids.studio.run.ui.wizards.creation.BaseNewWizardPage;

public class WeatherNewWizardPage extends BaseNewWizardPage {
	
	private IStructuredSelection selection;

	protected Text containerText;
	protected Text packageNameText;
	protected Text appNameText;
	protected Text appTypeNameText;
	protected Text discriptionText;

	protected String containerLabel;
	protected String appNameLabel;
	protected String appTypeLabel;

	protected String appName;
	protected String appType;
	protected String appDesciption;

	protected int numColumns = 2;

	/**
	 * 构造方法，含显示名称
	 * @param selection
	 * @param containerLabel
	 * @param newNameLabel
	 */
	public WeatherNewWizardPage(IStructuredSelection selection, String containerLabel, String newNameLabel) {
		this(selection, containerLabel, newNameLabel, RunMessages.Creation_BaseSSNewWizardPage_showNameLabel, false);
	}
	public WeatherNewWizardPage(IStructuredSelection selection) {
		this(selection, "位置", "微应用名称","微应用类型",false);
	}

	/**
	 * 构造方法，可以设置显示名称隐藏
	 * 
	 * @param selection
	 * @param containerLabel
	 * @param newNameLabel
	 * @param showNameLabel
	 *            {@code null}时不显示
	 */
	public WeatherNewWizardPage(IStructuredSelection selection, String containerLabel, String newNameLabel, String showNameLabel, boolean inSrc) {
		super(RunMessages.Creation_BaseNewWizardPage_title);
		this.selection = selection;
		this.containerLabel = containerLabel;
		this.appNameLabel = newNameLabel;
		this.appTypeLabel = showNameLabel;
		this.inSrc = inSrc;
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL | SWT.RESIZE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		layout.numColumns = numColumns;
		layout.verticalSpacing = 5;

		createContainerLabelText(composite);
		if (inSrc)
			createPackageLabelText(composite);
		createNewNameLabelText(composite);
		createAppTypeLabelText(composite);
		createDesciptionControl(composite);

		initialize();
		dialogChanged();
		setControl(composite);
		Dialog.applyDialogFont(composite);
		if (getHelpContextId() != null)
			PlatformUI.getWorkbench().getHelpSystem().setHelp(composite, getHelpContextId());
		setImageDescriptor(DialogImageProvider.DESC_DIALOG_TITLE);
	}

	protected void createContainerLabelText(Composite composite) {
		Label label = new Label(composite, SWT.NULL);
		label.setText(containerLabel);
		containerText = new Text(composite, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = numColumns - 1;
		containerText.setLayoutData(gd);
		containerText.setEnabled(false);
	}

	protected void createNewNameLabelText(Composite composite) {
		Label label = new Label(composite, SWT.NULL);
		label.setText(appNameLabel);
		appNameText = new Text(composite, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = numColumns - 1;
		appNameText.setLayoutData(gd);
		appNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				appName = appNameText.getText();
				dialogChanged();
			}
		});
		appNameText.setFocus();
	}

	protected void createPackageLabelText(Composite composite) {
		Label label = new Label(composite, SWT.NULL);
		label.setText("包路径");

		Composite container = new Composite(composite, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = numColumns - 1;
		container.setLayoutData(gd);
		container.setLayout(FormLayoutFactory.createClearGridLayout(false, 2));

		packageNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		packageNameText.setEnabled(false);
		packageNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		packageNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				packageName = packageNameText.getText();
				dialogChanged();
			}
		});
		Button btn = new Button(container, SWT.NONE);
		btn.setText("浏览...");
		btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ElementListSelectionDialog dialog = SelectionDialogUtils.makeModulePageckageSelectionDialog(getShell(), srcContainer, packageName);
				if (dialog.open() == ElementListSelectionDialog.OK) {
					packageNameText.setText(((DPackage) dialog.getFirstResult()).getName());
				}
			}
		});
	}

	/**
	 * 创建微应用类型
	 * @param composite
	 */
	protected void createAppTypeLabelText(Composite composite) {
		if (appTypeLabel == null)
			return;
		Label label = new Label(composite, SWT.NULL);
		label.setText(appTypeLabel);
		appTypeNameText = new Text(composite, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = numColumns - 1;
		appTypeNameText.setLayoutData(gd);
		appTypeNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				appType = appTypeNameText.getText();
				dialogChanged();
			}
		});
	}

	@Override
	protected boolean dialogChanged() {
		if (!super.dialogChanged())
			return false;
		if (appTypeLabel != null) {
			if (StringUtils.isNotEmpty(appType) && !FilenameValidationUtility.checkWinValid(appType)) {
				updateStatus(RunMessages.Creation_SimpleNewWizardPage_validate_showName_invalid);
				return false;
			}
		}
		updateStatus(null);
		return true;
	}

	protected void createDesciptionControl(Composite composite) {
		Label label = new Label(composite, SWT.NULL);
		label.setText("应用描述");
		discriptionText = new Text(composite, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = numColumns - 1;
		discriptionText.setLayoutData(gd);
		discriptionText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				appDesciption = discriptionText.getText();
				dialogChanged();
			}
		});
	}

	/**
	 * 初始化操作
	 */
	protected void initialize() {
		initSelectContainer(selection);
		if (resContainer != null)
			containerText.setText((inSrc ? srcContainer : resContainer).getFullPath().toString());
		if (packageName != null)
			packageNameText.setText(packageName);
	}

	public String getNewResourceName() {
		return appName;
	}

	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public String getAppDesciption() {
		return appDesciption;
	}
	public void setAppDesciption(String appDesciption) {
		this.appDesciption = appDesciption;
	}
	
	
}
