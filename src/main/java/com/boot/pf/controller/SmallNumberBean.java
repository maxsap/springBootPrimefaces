/**
 *  (C) 2013-2014 Stephan Rauh http://www.anderl.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.boot.pf.controller;

import com.boot.pf.repositories.EntityRepository;
import com.boot.pf.domain.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Component
@Scope("view")
public class SmallNumberBean {

    @Autowired
    EntityRepository testEntityDao;

	@Max(10)
	@Min(50)
   private int smallNumber = 42;

   public int getSmallNumber() {
      return smallNumber;
   }

   public void setSmallNumber(int smallNumber) {
      this.smallNumber = smallNumber;
   }

    @PreAuthorize("isFullyAuthenticated() and hasRole('ADMIN')")
   public void showErrors() {
	   FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "The low-priority message is displayed.");
	   FacesContext.getCurrentInstance().addMessage("smallNumberID", fm);
	   fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "This error message is oppressed, although it seems to be more important.");
	   FacesContext.getCurrentInstance().addMessage("smallNumberID", fm);

       Entity entity = new Entity();
       entity.setName("name");
       testEntityDao.save(entity);

       System.out.println("hgjghjgjhg");
   }

}