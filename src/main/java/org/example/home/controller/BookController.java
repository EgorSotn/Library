package org.example.home.controller;

import lombok.AllArgsConstructor;
import org.example.home.domain.Book;
import org.example.home.dto.BookDto;
import org.example.home.service.BookService;
import org.example.home.service.ConvertDto;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.ArrayList;
import java.util.List;


@Controller
@AllArgsConstructor
public class BookController {
    private final BookService bookService;
    private final ConvertDto convertDto;
    @GetMapping("/book/edit")
    public String editPage(@RequestParam("id") Long id, Model model) {

        Book book = bookService.getById(id);
        BookDto bookDto =convertDto.bookConvertToDto(book);
        model.addAttribute("bookDto", bookDto);
        return  "edit";
}

    @GetMapping("/book")
    public String getAllBool(Model model){

        List<BookDto> bookDtos = new ArrayList<>();
        bookService.getAllBook().stream().forEach(book -> bookDtos.add(convertDto.bookConvertToDto(book)));

        model.addAttribute("bookDtos", bookDtos);
        return "list";
    }
    @PostMapping("/book/add")
    public String addBook(@ModelAttribute("bookDto") BookDto bookDto) {

        Book book = convertDto.fromBookDtoToBook(bookDto);
        Book savedBook  = bookService.createBook(book);

        return "redirect:/book";
    }
    @GetMapping("/book/add")
    public String getHtmlCreateBook(Model model) {

        model.addAttribute("bookDto", new BookDto());
        return "create_book";
    }

    @PostMapping("/book/edit/delete")
    public String deleteBook(@RequestParam("id") Long id) {
        bookService.deleteBook(id);
        return "redirect:/book";
    }

    @PostMapping("/book/edit/update")
    public String updateBook(@RequestParam("id") Long id, @ModelAttribute("BookDto") BookDto bookDto ) {

        Book book = convertDto.fromBookDtoToBook(bookDto);
        bookService.updateBook(book);

        return "redirect:/book";
    }


}
