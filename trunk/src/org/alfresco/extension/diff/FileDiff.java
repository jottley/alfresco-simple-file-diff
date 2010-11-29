package org.alfresco.extension.diff;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.apache.commons.lang.StringEscapeUtils;

public class FileDiff extends DeclarativeWebScript {

	private ContentService contentService;
	private NodeService nodeService;

	private List<String> al1 = Collections.synchronizedList(new ArrayList<String>());
	private List<String> al2 = Collections.synchronizedList(new ArrayList<String>());

	public void setContentService(ContentService contentService) {
		this.contentService = contentService;
	}

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	private ContentReader getReader(NodeRef nodeRef) {

		return contentService.getReader(nodeRef, ContentModel.PROP_CONTENT);
	}

	@Override
	protected Map<String, Object> executeImpl(WebScriptRequest req,
			Status status, Cache cache) {

		Map<String, Object> model = new HashMap<String, Object>();

		Map<String, String> templateArgs = req.getServiceMatch()
				.getTemplateVars();

		NodeRef nodeRef1 = new NodeRef(templateArgs.get("store_type1"),
				templateArgs.get("store_id1"), templateArgs.get("id1"));

		model.put("file1name", nodeService.getProperty(nodeRef1,
				ContentModel.PROP_NAME));

		NodeRef nodeRef2 = new NodeRef(templateArgs.get("store_type2"),
				templateArgs.get("store_id2"), templateArgs.get("id2"));

		model.put("file2name", nodeService.getProperty(nodeRef2,
				ContentModel.PROP_NAME));

		ContentReader reader1 = getReader(nodeRef1);

		ContentReader reader2 = getReader(nodeRef2);

		try {

			DataInputStream in1 = new DataInputStream(reader1
					.getContentInputStream());
			BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));
			String strLine1;

			DataInputStream in2 = new DataInputStream(reader2
					.getContentInputStream());
			BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));
			String strLine2;

			// Read File Line By Line
			while ((strLine1 = br1.readLine()) != null) {
				al1.add(strLine1);
			}

			while ((strLine2 = br2.readLine()) != null) {
				al2.add(strLine2);
			}

			while (al1.size() < al2.size()) {
				al1.add(" ");
			}

			while (al2.size() < al1.size()) {
				al2.add(" ");
			}

			ArrayList<String> file1 = new ArrayList<String>();

			for (int i = 0; i < al1.size(); i++) {

				if (al1.get(i).hashCode() == al2.get(i).hashCode()) {
					file1.add("<div class=\"nodiff\"><pre>"
							+ StringEscapeUtils.escapeHtml(al1.get(i))
							+ "</pre></div>");
				} else {
					file1.add("<div class=\"diff\"><pre>"
							+ StringEscapeUtils.escapeHtml(al1.get(i))
							+ "</pre></div>");
				}

			}

			model.put("file1", file1);

			ArrayList<String> file2 = new ArrayList<String>();

			for (int i = 0; i < al1.size(); i++) {

				if (al2.get(i).hashCode() == al1.get(i).hashCode()) {
					file2.add("<div class=\"nodiff\"><pre>"
							+ StringEscapeUtils.escapeHtml(al2.get(i))
							+ "</pre></div>");
				} else {
					file2.add("<div class=\"diff\"><pre>"
							+ StringEscapeUtils.escapeHtml(al2.get(i))
							+ "</pre></div>");
				}

			}

			model.put("file2", file2);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated
			e.printStackTrace();
		} finally {

			al1.clear();
			al2.clear();

		}

		return model;
	}

}
