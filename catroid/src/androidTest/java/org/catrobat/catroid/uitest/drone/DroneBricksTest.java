/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2017 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.uitest.drone;

import android.util.Log;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.DroneBasicBrick;
import org.catrobat.catroid.content.bricks.DroneBasicControlBrick;
import org.catrobat.catroid.content.bricks.DroneBasicLookBrick;
import org.catrobat.catroid.content.bricks.DroneMoveBrick;
import org.catrobat.catroid.content.bricks.FormulaBrick;
import org.catrobat.catroid.test.drone.DroneTestUtils;
import org.catrobat.catroid.test.utils.TestUtils;
import org.catrobat.catroid.ui.ScriptActivity;
import org.catrobat.catroid.ui.SettingsActivity;
import org.catrobat.catroid.uitest.annotation.Device;
import org.catrobat.catroid.uitest.util.BaseActivityInstrumentationTestCase;
import org.catrobat.catroid.uitest.util.UiTestUtils;

public class DroneBricksTest extends BaseActivityInstrumentationTestCase<ScriptActivity> {

	private static final int TIME_IN_SECONDS_TO_CHANGE = 3;
	private static final int POWER_IN_PERCENT_TO_CHANGE = 40;

	public DroneBricksTest() {
		super(ScriptActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		TestUtils.deleteTestProjects();
		DroneTestUtils.createDefaultDroneProject();
		SettingsActivity.enableARDroneBricks(getActivity(), true);
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		SettingsActivity.enableARDroneBricks(getActivity(), false);
		TestUtils.deleteTestProjects();
		solo.finishOpenedActivities();
		super.tearDown();
	}

	@Device
	public void testAllMoveBricks() {
		int numberOfBricks = ProjectManager.getInstance().getCurrentScript().getBrickList().size();

		for (int count = 0; count < numberOfBricks; count++) {
			try {
				makeSingleBrickTest();
			} catch (NoSuchFieldException e) {
				Log.e(getClass().getSimpleName(), e.toString());
			}
		}
	}

	private void makeSingleBrickTest() throws NoSuchFieldException {

		if (ProjectManager.getInstance().getCurrentScript().getBrick(0) instanceof DroneMoveBrick) {

			FormulaBrick currentMoveBrick = (FormulaBrick) ProjectManager.getInstance().getCurrentScript().getBrick(0);

			assertNotNull("TextView does not exist.", solo.getView(R.id.brick_drone_move_text_view_second));

			UiTestUtils.testBrickWithFormulaEditor(solo, ProjectManager.getInstance().getCurrentSprite(),
					R.id.brick_drone_move_edit_text_second, TIME_IN_SECONDS_TO_CHANGE,
					Brick.BrickField.DRONE_TIME_TO_FLY_IN_SECONDS, currentMoveBrick);

			assertNotNull("TextView does not exist.", solo.getView(R.id.brick_drone_move_text_view_power));
			assertNotNull("TextView does not exist.", solo.getView(R.id.brick_set_power_to_percent));
			UiTestUtils.testBrickWithFormulaEditor(solo, ProjectManager.getInstance().getCurrentSprite(),
					R.id.brick_drone_move_edit_text_power, POWER_IN_PERCENT_TO_CHANGE,
					Brick.BrickField.DRONE_POWER_IN_PERCENT, currentMoveBrick);

			solo.clickOnView(solo.getView(R.id.brick_drone_move_label));
			solo.clickOnText(solo.getString(R.string.brick_context_dialog_delete_brick));
			solo.clickOnText(solo.getString(R.string.yes));
		} else if (ProjectManager.getInstance().getCurrentScript().getBrick(0) instanceof DroneBasicBrick) {
			solo.clickOnView(solo.getView(R.id.ValueTextView));
			solo.clickOnText(solo.getString(R.string.brick_context_dialog_delete_brick));
			solo.clickOnText(solo.getString(R.string.yes));
		} else if (ProjectManager.getInstance().getCurrentScript().getBrick(0) instanceof DroneBasicControlBrick) {
			solo.clickOnView(solo.getView(R.id.ValueTextViewControl));
			solo.clickOnText(solo.getString(R.string.brick_context_dialog_delete_brick));
			solo.clickOnText(solo.getString(R.string.yes));
		} else if (ProjectManager.getInstance().getCurrentScript().getBrick(0) instanceof DroneBasicLookBrick) {
			solo.clickOnView(solo.getView(R.id.ValueTextViewLook));
			solo.clickOnText(solo.getString(R.string.brick_context_dialog_delete_brick));
			solo.clickOnText(solo.getString(R.string.yes));
		}

		solo.sleep(350);
	}
}
