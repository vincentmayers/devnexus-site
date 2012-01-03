/*
 * Copyright 2002-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.devnexus.ting.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.devnexus.ting.core.model.Event;
import com.devnexus.ting.core.model.Presentation;
import com.devnexus.ting.core.model.PresentationList;
import com.devnexus.ting.core.service.BusinessService;

/**
 * Retrieves all jobs and returns an XML document. The structure conforms to the layout
 * defined by Indeed.com
 *
 * @author Gunnar Hillert
 * @version $Id:UserService.java 128 2007-07-27 03:55:54Z ghillert $
 */
@Controller
public class PresentationController {

    @Autowired private BusinessService businessService;

    /** serialVersionUID. */
    private static final long serialVersionUID = -3422780336408883930L;

    private final static Logger LOGGER = LoggerFactory.getLogger(PresentationController.class);

    @RequestMapping("/{eventKey}/presentations")
    public String getPresentationsForEvent(@PathVariable("eventKey") final String eventKey,
                                           final Model model,
                                           final SitePreference sitePreference) {
        final Event event = businessService.getEventByEventKey(eventKey);
        model.addAttribute("event", event);

        final PresentationList presentationList = new PresentationList();
        presentationList.setPresentations(businessService.getPresentationsForEvent(event.getId()));

        model.addAttribute("presentationList", presentationList);

        if (sitePreference.isMobile()) {
            return "presentations-mobile";
        }

        return "presentations";
    }

    @RequestMapping(value="/presentations/{presentationId}/slides", method=RequestMethod.GET)
    public void getPresentationSlides(@PathVariable("presentationId") Long presentationId, HttpServletResponse response) {

        final Presentation presentation = businessService.getPresentation(presentationId);

        if (presentation.getPresentationFile() != null) {

            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition",
                    "attachment; filename=\"" + presentation.getPresentationFile().getName() + "\"");
            response.setContentLength(presentation.getPresentationFile().getFileSize().intValue());
            try {
                IOUtils.write(presentation.getPresentationFile().getFileData(),response.getOutputStream());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }

        } else {
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

    }

    @RequestMapping("/presentations")
    public String getPresentationsForCurrentEvent(final Model model,
                                                  final SitePreference sitePreference) {

        final PresentationList presentationList = new PresentationList();
        presentationList.setPresentations(businessService.getPresentationsForCurrentEvent());



        model.addAttribute("presentationList", presentationList);

        if (sitePreference.isMobile()) {
            return "presentations-mobile";
        }

        return "presentations";
    }

}