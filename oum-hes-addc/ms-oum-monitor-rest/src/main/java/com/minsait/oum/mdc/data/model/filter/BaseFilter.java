package com.minsait.oum.mdc.data.model.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseFilter {

	public static final String SORT_ORDER_ASC = "ASC";
	public static final String SORT_ORDER_DESC = "DESC";
	
	public static final Integer DEFAULT_FIRST_RESULT = 0; 
	public static final Integer DEFAULT_SIZE_NO = 20; 
	public static final String DEFAULT_SORT_FIELD = "_id"; 
	public static final String DEFAULT_SORT_ORDER = SORT_ORDER_DESC; 
	
	private String id;

	private Integer firstResult = DEFAULT_FIRST_RESULT;
	private Integer sizeNo = DEFAULT_SIZE_NO;
	private String sortFieldName = DEFAULT_SORT_FIELD;
	private String sortOrder = DEFAULT_SORT_ORDER;
	private String orderName;
	
	
	public BaseFilter(String id) {
		super();
		this.id = id;
	}
	
}
