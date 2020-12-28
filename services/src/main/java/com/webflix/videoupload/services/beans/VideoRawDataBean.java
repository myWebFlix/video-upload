package com.webflix.videoupload.services.beans;

import com.webflix.videoupload.models.entities.VideoRawDataEntity;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@RequestScoped
public class VideoRawDataBean {

	@PersistenceContext(unitName = "webflix-jpa")
	private EntityManager em;

	public List<VideoRawDataEntity> getVideoRawData(){

		TypedQuery<VideoRawDataEntity> query = em.createNamedQuery("VideoRawDataEntity.getAll", VideoRawDataEntity.class);

		return query.getResultList();
	}

	public List<VideoRawDataEntity> getVideoRawData(String videoId){
		return em.createQuery("SELECT vrd FROM VideoRawDataEntity vrd WHERE vrd.video_id LIKE :video_id", VideoRawDataEntity.class)
				.setParameter("video_id", videoId)
				.getResultList();
	}

	// Transactions

	private void beginTx() {
		if (!em.getTransaction().isActive())
			em.getTransaction().begin();
	}

	private void commitTx() {
		if (em.getTransaction().isActive())
			em.getTransaction().commit();
	}

	private void rollbackTx() {
		if (em.getTransaction().isActive())
			em.getTransaction().rollback();
	}

}
