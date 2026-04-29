package com.Lifelink.HeathCareBridge.service;

import com.Lifelink.HeathCareBridge.payload.DonorRequestDTO;
import com.Lifelink.HeathCareBridge.payload.DonorResponseDTO;

public interface DonorService {

    DonorResponseDTO registerDonor(DonorRequestDTO donorRequestDTO);
}
