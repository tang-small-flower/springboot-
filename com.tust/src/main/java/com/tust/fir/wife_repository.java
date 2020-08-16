package com.tust.fir;
import org.springframework.data.jpa.repository.JpaRepository;
public interface wife_repository extends JpaRepository<wife,Integer>{
	public wife findByName(String name);

}
