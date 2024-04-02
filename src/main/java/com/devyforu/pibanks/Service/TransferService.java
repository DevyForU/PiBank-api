package com.devyforu.pibanks.Service;

import com.devyforu.pibanks.Model.Transfer;
import com.devyforu.pibanks.Repository.TransferDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TransferService {
    private TransferDAO transferDAO;

    public List<Transfer> findAll(){
        return transferDAO.findAll();
    }

    public Transfer save(Transfer toSave){
        return transferDAO.save(toSave);
    }

    public void deleteById(String id){
         transferDAO.deleteById(id);
    }

//    public Transfer updateTransferReasonById(String id, String transferReason){
//        return transferDAO.updateTransferReasonById(id, transferReason);
//    }

    public Transfer getById(String id){
        return transferDAO.getById(id);
    }

}
