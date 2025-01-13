package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {

    // 검증 객체가 맞는지 확인
    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
        // item == clazz 와 같냐를 물어보는 것
        // item == subItem 아이템의 자식 클래스드 검증 허락
    }

    // 검증 로직
    @Override
    public void validate(Object target, Errors errors) {
        Item item = (Item) target;

        // 필드 검증 로직 => 같은거
//        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");
        // 필드 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            // 상품명 유무
            errors.rejectValue("itemName", "required");
        }
        if ( item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            // 가격 범위
            errors.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }
        if ( item.getQuantity() == null || item.getQuantity() >= 9999) {
            // 수량 최대수 제한
            errors.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10000) {
                errors.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }
    }
}
