package com.example.BestBid.BestBid.Repositorys;


import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.BestBid.BestBid.Models.Bid;

public interface BidRepository extends PagingAndSortingRepository<Bid,Integer>{
	
	public List<Bid>  getBidsByProjectId(Integer projectId);

	public Page<Bid>  getBidsByProjectId(Integer projectId,Pageable pageable);
	
	public Page<Bid> getBidsByUserId(Integer ownerId,Pageable pageable);

	@Transactional
	public void deleteByProjectId(int projectId);
	
	@Query("SELECT b FROM Bid b WHERE bidAmount=(SELECT MIN(bidAmount) from Bid WHERE projectId=?1)")
	public List<Bid> getLowestBidForProject(Integer projectId);
	
}
