package com.trilogyed.gamestoreinvoicing.feign;

import com.trilogyed.gamestoreinvoicing.viewModel.ConsoleViewModel;
import com.trilogyed.gamestoreinvoicing.viewModel.GameViewModel;
import com.trilogyed.gamestoreinvoicing.viewModel.TShirtViewModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "gamestore-catalog")
public interface GameStoreCatalog {

    //console interfaces
    @RequestMapping(value = "/console", method = RequestMethod.GET)
    public List<ConsoleViewModel> getAllConsoles();

    @RequestMapping(value = "/console/{id}", method = RequestMethod.GET)
    public ConsoleViewModel getConsole(@PathVariable("id") long consoleId);

    @RequestMapping(value = "/console", method = RequestMethod.POST)
    public ConsoleViewModel createConsole(ConsoleViewModel consoleViewModel);

    @RequestMapping(value = "/console", method = RequestMethod.PUT)
    public ConsoleViewModel updateConsole(ConsoleViewModel consoleViewModel);

    @RequestMapping(value = "/console/{id}", method = RequestMethod.DELETE)
    public ConsoleViewModel deleteConsole(@PathVariable("id") long consoleId);

    //game interfaces
    @RequestMapping(value = "/game", method = RequestMethod.GET)
    public List<GameViewModel> getAllGames();

    @RequestMapping(value = "/game/{id}", method = RequestMethod.GET)
    public GameViewModel getGame(@PathVariable("id") long gameId);

    @RequestMapping(value = "/game", method = RequestMethod.POST)
    public GameViewModel createGame(GameViewModel gameViewModel);

    @RequestMapping(value = "/game", method = RequestMethod.PUT)
    public GameViewModel updateGame(GameViewModel gameViewModel);

    @RequestMapping(value = "/game/{id}", method = RequestMethod.DELETE)
    public GameViewModel deleteGame(@PathVariable("id") long gameId);

    //TShirt interfaces
    @RequestMapping(value = "/tshirt", method = RequestMethod.GET)
    public List<TShirtViewModel> getAllTShirts();

    @RequestMapping(value = "/tshirt/{id}", method = RequestMethod.GET)
    public TShirtViewModel getTShirt(@PathVariable("id") long tshirtId);

    @RequestMapping(value = "/tshirt", method = RequestMethod.POST)
    public TShirtViewModel createTShirt(TShirtViewModel tshirtViewModel);

    @RequestMapping(value = "/tshirt", method = RequestMethod.PUT)
    public TShirtViewModel updateTShirt(TShirtViewModel tshirtViewModel);

    @RequestMapping(value = "/tshirt/{id}", method = RequestMethod.DELETE)
    public TShirtViewModel deleteTShirt(@PathVariable("id") long tshirtId);
}
