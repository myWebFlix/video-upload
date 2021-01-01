package com.webflix.videoupload.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "video_raw_data")
@NamedQueries(value =
		{
				@NamedQuery(name = "VideoRawDataEntity.getAll",
						query = "SELECT vrd FROM VideoRawDataEntity vrd")
		})
public class VideoRawDataEntity {

	// Fields

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "video_id")
	private Integer video_id;

	@Column(name = "url")
	private String url;

	@Column(name = "quality")
	private String quality;

	// Getters & Setters

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVideo_id() {
		return video_id;
	}

	public void setVideo_id(Integer video_id) {
		this.video_id = video_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

}
