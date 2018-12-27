package com.example.BestBid.BestBid.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.BestBid.BestBid.Models.Bid;
import com.example.BestBid.BestBid.Repositorys.BidRepository;

@Service
public class BidService {

	@Autowired
	private BidRepository BidRepository;
	
	public Bid addBid(Bid bid){
		return BidRepository.save(bid);
	}
	
	public Optional<Bid> getbid(Integer bidId) {
		return BidRepository.findById(bidId);
	}
	
	public List<Bid> getLowestBidForProject(Integer projectId) {
		return BidRepository.getLowestBidForProject(projectId);
	}
	
	public List<Bid> getBidsByProjectId(Integer projectId) {
		return BidRepository.getBidsByProjectId(projectId);
	}
	
	public Page<Bid> getBidsByProjectId(Integer projectId,Pageable pageable) {
		return BidRepository.getBidsByProjectId(projectId,pageable);
	}
	
	public Page<Bid> getBidsByUserId(Integer userId,Pageable pageable) {
		return BidRepository.getBidsByUserId(userId,pageable);
	}

	public void deleteBid(Bid bid) {
		BidRepository.delete(bid);		
	}

	public void deleteBidsForProjectId(int projectId) {
		BidRepository.deleteByProjectId(projectId);
	}
}
