/*
 * #%L
 * share-po
 * %%
 * Copyright (C) 2005 - 2016 Alfresco Software Limited
 * %%
 * This file is part of the Alfresco software. 
 * If the software was purchased under a paid Alfresco license, the terms of 
 * the paid license agreement will prevail.  Otherwise, the software is 
 * provided under the following open source license terms:
 * 
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
package org.alfresco.po.share;

import static org.alfresco.po.RenderElement.getVisibleRenderElement;

import org.alfresco.po.HtmlPage;
import org.alfresco.po.RenderTime;
import org.alfresco.po.exception.PageException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

/**
 * @author Olga Antonik
 */
public class EditGroupPage extends SharePage
{
    private static Log logger = LogFactory.getLog(EditGroupPage.class);
    private static final By UPDATE_DISPLAYNAME_INPUT = By.cssSelector("input[id*='update-displayname']");
    private static final By SAVE_CHANGES_BUTTON = By.cssSelector("button[id*='updategroup-save-button-button']");
    private static final By CANCEL_BUTTON = By.cssSelector("button[id*='updategroup-cancel-button-button']");

    public enum ActionButton
    {
        SAVE, CANCEL;
    }

    @SuppressWarnings("unchecked")
    @Override
    public EditGroupPage render(RenderTime timer)
    {
        elementRender(timer, getVisibleRenderElement(UPDATE_DISPLAYNAME_INPUT), getVisibleRenderElement(SAVE_CHANGES_BUTTON),
                getVisibleRenderElement(CANCEL_BUTTON));
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public EditGroupPage render()
    {
        return render(new RenderTime(maxPageLoadingTime));
    }
    GroupsPage groupsPage;
    /**
     * Edit group
     * 
     * @param newGroupName -new displayed name of group
     * @param edit - 'true' if Save Changes button must be press, 'false' - Cancel button
     * @return GroupsPage
     */
    protected GroupsPage editGroup(String newGroupName, boolean edit)
    {

        try
        {
            if (StringUtils.isEmpty(newGroupName))
            {
                throw new IllegalArgumentException("Group Name is required.");
            }

            WebElement updateField = findAndWait(UPDATE_DISPLAYNAME_INPUT);
            updateField.clear();
            updateField.sendKeys(newGroupName);

            if (edit)
            {
                WebElement saveButton = findAndWait(SAVE_CHANGES_BUTTON);
                saveButton.click();
            }
            else
            {
                WebElement cancel = driver.findElement(CANCEL_BUTTON);
                cancel.click();
            }

            return groupsPage.render();
        }

        catch (TimeoutException e)
        {
            if (logger.isTraceEnabled())
            {
                logger.trace("Group can not be edited", e);
            }
        }
        throw new PageException("Group can not be edited.");

    }

    /**
     * Change of the desplayed name of a group
     * 
     * @param displayName - new name
     */
    public void setDisplayName(String displayName)
    {
        if (StringUtils.isEmpty(displayName))
        {
            throw new PageException("Enter value of DisplayName");
        }
        WebElement input = findAndWait(UPDATE_DISPLAYNAME_INPUT);
        input.clear();
        input.sendKeys(displayName);
    }
    /**
     * Save or cancel action button
     * 
     * @param groupButton ActionButton
     * @return HtmlPage
     */
    public HtmlPage clickButton(ActionButton groupButton)
    {
        switch (groupButton)
        {
            case SAVE:
                findAndWait(SAVE_CHANGES_BUTTON).click();
                canResume();
                return groupsPage;

            case CANCEL:
                findAndWait(CANCEL_BUTTON).click();
                canResume();
                return this;

        }
        throw new PageException("Wrong Page");

    }

    /**
     * Checks if Save button is present and enabled
     * 
     * @return boolean
     */
    public boolean isSaveButtonEnabled()
    {
        try
        {
            WebElement searchButton = driver.findElement(SAVE_CHANGES_BUTTON);
            return searchButton.isDisplayed() && searchButton.isEnabled();
        }
        catch (NoSuchElementException e)
        {
            throw new PageException("Not found Element:" + SAVE_CHANGES_BUTTON, e);
        }
    }

    /**
     * Checks if Display name filed is present
     * 
     * @return boolean
     */
    public boolean isDisplayNameInputPresent()
    {
        try
        {
            WebElement searchButton = driver.findElement(UPDATE_DISPLAYNAME_INPUT);
            return searchButton.isDisplayed();
        }
        catch (NoSuchElementException e)
        {
            throw new PageException("Not found Element:" + UPDATE_DISPLAYNAME_INPUT, e);
        }
    }

}
