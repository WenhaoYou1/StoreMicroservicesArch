package com.wenhao.bookstore.orders.web.controllers;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Named.named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wenhao.bookstore.orders.domain.OrderService;
import com.wenhao.bookstore.orders.domain.SecurityService;
import com.wenhao.bookstore.orders.domain.models.CreateOrderRequest;
import static com.wenhao.bookstore.orders.testdata.TestDataFactory.createOrderRequestWithInvalidCustomer;
import static com.wenhao.bookstore.orders.testdata.TestDataFactory.createOrderRequestWithInvalidDeliveryAddress;
import static com.wenhao.bookstore.orders.testdata.TestDataFactory.createOrderRequestWithNoItems;
// Note: 当你知道问题出在controller层的时候可以用unit test, 也可以用它测测API入参情况/路径/status code/@Valid等.
// @WebMvcTest: 只加载 Web 层，不加载整个应用（只测试 controller，而不连接数据库等）
// Mockito：用来“伪造” service 层逻辑（不走真实业务逻辑）
@WebMvcTest(OrderController.class) // 告诉 Spring 只加载 OrderController 所需的 bean，不会加载 Service 和 Repository
class OrderControllerUnitTests {
    @MockBean
    private OrderService orderService;

    @MockBean
    private SecurityService securityService;

    @Autowired
    private MockMvc mockMvc; // MockMvc：模拟 HTTP 请求（不是真实服务器）

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        given(securityService.getLoginUserName()).willReturn("siva");
    }

    @ParameterizedTest(name = "[{index}]-{0}")
    @MethodSource("createOrderRequestProvider")
    void shouldReturnBadRequestWhenOrderPayloadIsInvalid(CreateOrderRequest request) throws Exception {
        given(orderService.createOrder(eq("siva"), any(CreateOrderRequest.class)))
                .willReturn(null);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    static Stream<Arguments> createOrderRequestProvider() {
        return Stream.of(
                arguments(named("Order with Invalid Customer", createOrderRequestWithInvalidCustomer())),
                arguments(named("Order with Invalid Delivery Address", createOrderRequestWithInvalidDeliveryAddress())),
                arguments(named("Order with No Items", createOrderRequestWithNoItems())));
    }
}
