package org.example.home.controller;

import lombok.val;
import org.example.home.domain.Author;
import org.example.home.domain.Book;
import org.example.home.domain.Genre;
import org.example.home.dto.BookDto;
import org.example.home.service.BookService;
import org.example.home.service.ConvertDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)

@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
class BookControllerTest {
    private static final Long ID_BOOK1 = 1l;
    private static final String BOOK_NAME1 = "Eva";
    private static final String BOOK_YEAR1 = "2000-01-01";
    private static final String GENRE_NAME1 = "Drama";
    private static final String NAME_AUTHOR1 = "Pushkin";
    private static final String YEAR_AUTHOR1 = "1899-05-22";

    @MockBean
    BookService bookService;

    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @MockBean
    ConvertDto convertDto;
    MockMvc mvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @BeforeEach
    public void setUp(){
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }


    @Test
    void editPage() throws Exception {
        val bookDto = new BookDto(ID_BOOK1, BOOK_NAME1, BOOK_YEAR1, NAME_AUTHOR1,
                YEAR_AUTHOR1, GENRE_NAME1);
        val book = new Book(ID_BOOK1,BOOK_NAME1, BOOK_YEAR1,
                new Genre(GENRE_NAME1), new Author(NAME_AUTHOR1, YEAR_AUTHOR1));

        given(bookService.getById(any())).willReturn(book);
        given(convertDto.bookConvertToDto(any())).willReturn(bookDto);

        mvc.perform(get("/book/edit", bookDto.getIdBook())
                        .param("id", ID_BOOK1.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("bookDto", bookDto))
                .andExpect(view().name("edit"));
    }


    @Test
    @WithMockUser(username = "user", authorities = "ROLE_USER")
    void getAllBook() throws Exception {
        given(bookService.getAllBook()).willReturn(List.of(new Book(ID_BOOK1,BOOK_NAME1, BOOK_YEAR1,
                        new Genre(GENRE_NAME1), new Author(NAME_AUTHOR1, YEAR_AUTHOR1))));

        given(convertDto.bookConvertToDto(any())).willReturn(new BookDto(ID_BOOK1, BOOK_NAME1, BOOK_YEAR1, NAME_AUTHOR1,
                        YEAR_AUTHOR1, GENRE_NAME1));


        List<BookDto> bookDtoList = new ArrayList<>();
        bookDtoList.add(convertDto.bookConvertToDto(any()));


        mvc.perform(get("/book")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("bookDtos", bookDtoList))
                .andExpect(view().name("list"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void addBook() throws Exception {
        val bookDto = new BookDto(ID_BOOK1, BOOK_NAME1, BOOK_YEAR1, NAME_AUTHOR1,
                YEAR_AUTHOR1, GENRE_NAME1);

        val book = new Book(ID_BOOK1,BOOK_NAME1, BOOK_YEAR1,
                new Genre(GENRE_NAME1), new Author(NAME_AUTHOR1, YEAR_AUTHOR1));

//        given(parsingDtoBook.BookDto_to_Book(bookDto)).willReturn(book);
        given(convertDto.fromBookDtoToBook(any())).willReturn(book);
        given(bookService.createBook(any()))
                .willReturn(book);

//        assertThat(parsingDtoBook.BookDto_to_Book(any())).isNotEqualTo(book);
        mvc.perform(post("/book/add", bookDto)
                        .flashAttr("bookDto", bookDto)
                ).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void getHtmlCreateBook() throws Exception {
        mvc.perform(get("/book/add")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("bookDto", new BookDto()))
                .andExpect(view().name("create_book"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void deleteBook() throws Exception {
        mvc.perform(post("/book/edit/delete", ID_BOOK1).param("id", ID_BOOK1.toString())).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void updateBook() throws Exception {
        val bookDto = new BookDto(ID_BOOK1, BOOK_NAME1, BOOK_YEAR1, NAME_AUTHOR1,
                YEAR_AUTHOR1, GENRE_NAME1);
        val book = new Book(ID_BOOK1,BOOK_NAME1, BOOK_YEAR1,
                new Genre(GENRE_NAME1), new Author(NAME_AUTHOR1, YEAR_AUTHOR1));
        given(convertDto.fromBookDtoToBook(any())).willReturn(book);
        given(bookService.updateBook(any())).willReturn(book);

        mvc.perform(post("/book/edit/update", bookDto).param("id", Long.toString(bookDto.getIdBook()))
                        .flashAttr("BookDto", bookDto))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book"));
    }
}