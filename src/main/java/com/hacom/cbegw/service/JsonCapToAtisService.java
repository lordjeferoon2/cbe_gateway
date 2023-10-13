package com.hacom.cbegw.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hacom.cbegw.entity.Alert;
import com.hacom.cbegw.entity.Area;
import com.hacom.cbegw.entity.CBEM_CBS_Broadcast_Text;
import com.hacom.cbegw.entity.CBEM_CBS_Geotargeting_Info;
import com.hacom.cbegw.entity.CBEM_CBS_Message_Info;
import com.hacom.cbegw.entity.CBEM_CBS_Request;
import com.hacom.cbegw.entity.Info;
import com.hacom.cbegw.entity.Resource;

@Service
public class JsonCapToAtisService {
	
	public CBEM_CBS_Request jsonCapToAtis(Alert alert) {
        CBEM_CBS_Request cbemcbsrequest = new CBEM_CBS_Request();
        
        cbemcbsrequest.setCbem_sender_id(alert.getSender());
        cbemcbsrequest.setCbem_protocol_version("2.0");
        cbemcbsrequest.setCbem_message_number("HACOM-2019-0001");
        cbemcbsrequest.setCbem_message_id("4380");
        
        switch(alert.getMsgType()) {
        	case "Alert": cbemcbsrequest.setCbem_message_type("CBS Request"); break;
        }
        
        List<CBEM_CBS_Message_Info> cbemcbsMessageinfoList  = new ArrayList<CBEM_CBS_Message_Info>();
        for(int i = 0; i < alert.getInfo().size(); i++) {
        	Info info = alert.getInfo().get(i);
        	CBEM_CBS_Message_Info cbemcbsMessageinfo = new CBEM_CBS_Message_Info();
        	
        	cbemcbsMessageinfo.setCbem_language(info.getLanguage());
        	cbemcbsMessageinfo.setCbem_start_data_time(info.getEffective());
        	cbemcbsMessageinfo.setCbem_data_coding_scheme("GSM_7_Bit_Coding");
            cbemcbsMessageinfo.setCbem_repetition_period(50);
            cbemcbsMessageinfo.setCbem_number_broadcast_requested(10);
            
            switch(info.getUrgency()) {
            	case "Immediate": cbemcbsMessageinfo.setCbem_display_mode("immediate"); break;
            	default: cbemcbsMessageinfo.setCbem_display_mode("normal"); break;
            }
        	
        	List<CBEM_CBS_Geotargeting_Info> CBEMCBSGeotargetingInfoList  = new ArrayList<CBEM_CBS_Geotargeting_Info>();
        	for(int j = 0; j < alert.getInfo().get(i).getArea().size(); j++) {
        		Area area = alert.getInfo().get(i).getArea().get(j);
        		CBEM_CBS_Geotargeting_Info cbemcbsGeotargetingInfo = new CBEM_CBS_Geotargeting_Info();
        		
        		cbemcbsGeotargetingInfo.setCbem_geocode_type(area.getAreaDesc());
        		cbemcbsGeotargetingInfo.setCbem_polygon(area.getPolygon());
        		cbemcbsGeotargetingInfo.setCbem_circle(area.getCircle());
        		
        		CBEMCBSGeotargetingInfoList.add(cbemcbsGeotargetingInfo);
        	}
        	
        	List<CBEM_CBS_Broadcast_Text> cbemcbsBroadcastTextList  = new ArrayList<CBEM_CBS_Broadcast_Text>();
        	for(int k = 0; k < alert.getInfo().get(i).getResource().size(); k++) {
        		Resource resource = alert.getInfo().get(i).getResource().get(k);
        		CBEM_CBS_Broadcast_Text cbemcbsBroadcastText = new CBEM_CBS_Broadcast_Text();
        		
        		cbemcbsBroadcastTextList.add(cbemcbsBroadcastText);
        	}
        	
        	cbemcbsMessageinfo.setCbem_cbs_geotargeting_info(CBEMCBSGeotargetingInfoList);
        	cbemcbsMessageinfo.setCbem_cbs_broadcast_text(cbemcbsBroadcastTextList);
        	cbemcbsMessageinfoList.add(cbemcbsMessageinfo);
        }
        cbemcbsrequest.setCbem_info(cbemcbsMessageinfoList);
		
		return cbemcbsrequest;
    }
	
}
