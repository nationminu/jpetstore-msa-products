package com.rock.jpetstore.domain;
 
import javax.persistence.Entity;
import javax.persistence.Id; 
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Products {

	@Id
	private String productId;
	private String categoryId;
	private String name;
	private String description;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId.trim();
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return getName();
	}
}
