package com.bts.adamcrm.database;

import com.bts.adamcrm.model.Attachment;
import com.bts.adamcrm.model.Category;
import com.bts.adamcrm.model.Customer;
import com.bts.adamcrm.model.StockItem;

import java.util.List;

public class QueryContract {
    public interface CustomerQuery {
        void insertCustomer(Customer customer, QueryResponse<Customer> response);
        void getCustomerById(int customerId, QueryResponse<Customer> response);
        void getAllCustomers(QueryResponse<List<Customer>> response);
        void updateCustomerInfo(Customer customer, QueryResponse<Boolean> response);
        void deleteCustomerById(int CustomerId, QueryResponse<Boolean> response);
        void deleteAllCustomers(QueryResponse<Boolean> response);
    }

    public interface CategoryQuery {
        void insertCategory(Category category, QueryResponse<Category> response);
        void getAllCategories(QueryResponse<List<Category>> response);
        void getCategoryById(int categoryId, QueryResponse<Category> response);
        void updateCategoryInfo(Category category, QueryResponse<Boolean> response);
        void deleteCategoryById(int CategoryId, QueryResponse<Boolean> response);
        void deleteAllCategories(QueryResponse<Boolean> response);
    }

    public interface AttachmentQuery {
        void insertAttachment(Attachment attachment, QueryResponse<Attachment> response);
        void getAllAttachments(QueryResponse<List<Attachment>> response);
        void getAttachmentById(int attachmentId, QueryResponse<Attachment> response);
        void updateAttachmentInfo(Attachment attachment, QueryResponse<Boolean> response);
        void deleteAttachmentById(int AttachmentId, QueryResponse<Boolean> response);
        void deleteAllAttachments(QueryResponse<Boolean> response);
    }

    public interface StockQuery {
        void insertStock(StockItem stock, QueryResponse<Boolean> response);
        void getAllStocks(QueryResponse<List<StockItem>> response);
        void getStockById(int stockId, QueryResponse<StockItem> response);
        void updateStockInfo(StockItem stock, QueryResponse<Boolean> response);
        void deleteStockById(int StockId, QueryResponse<Boolean> response);
        void deleteAllStocks(QueryResponse<Boolean> response);
    }
}
