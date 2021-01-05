package com.webflix.videoupload.services.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("app-config")
public class AppConfig {
	@ConfigValue("gcloud-storage.project-id")
	private String projectId;

	@ConfigValue("gcloud-storage.bucket-name")
	private String bucketName;


	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
}
