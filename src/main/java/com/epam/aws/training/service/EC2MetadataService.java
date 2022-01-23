package com.epam.aws.training.service;

import com.epam.aws.training.dto.EC2Metadata;

public interface EC2MetadataService {

  EC2Metadata retrieveMetadata();
}
