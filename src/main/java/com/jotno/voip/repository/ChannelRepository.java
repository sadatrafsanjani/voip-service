package com.jotno.voip.repository;

import com.jotno.voip.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {

    Channel findByChannelArn(String channelArn);
}
