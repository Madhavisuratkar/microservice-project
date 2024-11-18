package com.programmingtechie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.programmingtechie.entity.Inventory;

import java.util.List;
import java.util.Optional;


@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

	
	Optional<Inventory> findBySkuCodeIn(List<String> skuCodes);
}
