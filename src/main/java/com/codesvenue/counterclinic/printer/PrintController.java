package com.codesvenue.counterclinic.printer;

import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointmentWrapper;
import com.codesvenue.counterclinic.walkinappointment.service.WalkInAppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/print")
public class PrintController {

    private WalkInAppointmentService walkInAppointmentService;

    public PrintController(WalkInAppointmentService walkInAppointmentService) {
        this.walkInAppointmentService = walkInAppointmentService;
    }

    @GetMapping("/{appointmentId}/walk-in-appointment")
    public ModelAndView printWalkInAppointmentView(@PathVariable("appointmentId") int appointmentId) {
        WalkInAppointmentWrapper walkInAppointmentWrapper = walkInAppointmentService.getWrappedAppointment(appointmentId);
        ModelAndView mav = new ModelAndView(View.MASTER_TEMPLATE);
        mav.addObject("walkInAppointmentWrapper", walkInAppointmentWrapper);
        mav.addObject(new View("print", "print_appointment"));
        return mav;
    }
}
