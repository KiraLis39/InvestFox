package ru.investment.parser.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import ru.investment.parser.enums.ParserEvents;
import ru.investment.parser.enums.ParserStates;

@Slf4j
@Component
@RequiredArgsConstructor
public class PurchaseList_ParseAction implements Action<ParserStates, ParserEvents> {

    @Override
    public void execute(final StateContext<ParserStates, ParserEvents> context) {
//        parserStateService.saveCurrentStateAndTaskByContext(context);
//
//        guid = context.getExtendedState().get("guid", String.class); // TODO: избавиться от переменных контекста для реализации нормальной многопоточности
//        parserTaskDTO = parserTaskService.findOneByGuid(guid).orElseThrow();
//
//        try {
//            // обработка коллекции закупок очередной страницы:
//            Boolean shouldReparseAll = context.getExtendedState().get("shouldReparseAll", Boolean.class); // warn: context used!
//            if (shouldReparseAll != null && shouldReparseAll) {
//                doReparseAll(context);
//                return;
//            }
//
//            if (!isDepthAllowToParseNext()) {
//                log.debug("Max depth reached so stop the search.");
//                context.getStateMachine().sendEvent(ParserEvents.MAX_DEPTH_REACHED);
//                return;
//            }
//
//            // прогрузочный манёвр:
//            $x("/html/body").shouldBe(Condition.visible);
//
//            // парсим лист и узнаём были ли новые элементы:
//            if (scanPageAndCheckNewElements()) {
//                context.getStateMachine().sendEvent(ParserEvents.NEW_ITEMS_FOUND_AND_SAVED);
//            } else {
//                context.getStateMachine().sendEvent(ParserEvents.NO_NEW_ITEMS_BUT_GO_DEEPER);
//            }
//            parserTaskDTO.increaseParsedPagesCount(); // увеличиваем проход страниц для видимости прогресса.
//        } catch (NoSuchSessionException nss) {
//            log.warn("Сессия не создана или потерялась? Ошибка: " + nss.getMessage());
//            handleErrorService.saveErrorInfo(context, nss);
//            context.getStateMachine().sendEvent(ParserEvents.ERROR_OCCURRED);
//        } catch (DataIntegrityViolationException dive) {
//            log.error(
//                "Запущено более одного потока? Текущая стейтмашина будет остановлена для дальнейего предотвращения ошибки: " +
//                dive.getMessage(),
//                dive
//            );
//            PurchaseList_SetupAndLoadAction.decreasePage(); // откатываем номер страницы, т.к. захвативший её поток не справился с задачей.
//            handleErrorService.saveErrorInfo(context, dive, "Текущая стейтмашина будет остановлена");
//            context.getStateMachine().stop();
//        } catch (WebDriverException wde) {
//            handleErrorService.saveErrorInfo(context, wde, "Ошибка драйвера");
//            if (wde.getMessage().contains("net::ERR_CONNECTION_TIMED_OUT")) {
//                throw new NetConnectException("Похоже, нет доступа к интернету. Проверьте возможность подключения.", wde);
//            } else {
//                context.getStateMachine().sendEvent(ParserEvents.ERROR_OCCURRED);
//                throw wde;
//            }
//        } catch (Throwable e) {
//            if (e.getCause() instanceof InterruptedException) {
//                log.error("Работа потока была прервана: " + e.getMessage());
//                handleErrorService.saveErrorInfo(context, "Работа потока была прервана");
//                context.getStateMachine().sendEvent(ParserEvents.ACCOMPLISHED);
//            } else {
//                log.error("Возникла непредвиденная ошибка: " + e.getMessage(), e);
//                handleErrorService.saveErrorInfo(context, e, "Непредвиденная ошибка: " + e.getMessage());
//                context.getStateMachine().sendEvent(ParserEvents.ERROR_OCCURRED);
//            }
//        }
    }

//    private boolean scanPageAndCheckNewElements() throws UnknownParseException {
//        log.debug("Checking for new page elements to save...");
//
//        ArrayList<SelenideElement> cards = new ArrayList<>(props.getPurchasesDefaultPageSize());
//        Set<PurchasePage> foundSetToSave = new HashSet<>(props.getPurchasesDefaultPageSize());
//        int newItemsCount = 0;
//        boolean isNewItemsFound = false;
//
//        ElementsCollection purchaseUrlElements = $$(
//            By.xpath("//*[@id='quickSearchForm_header']/section[2]/div/div/div[1]/div[3]/div/div/div[1]/div[1]")
//        );
//        if (purchaseUrlElements.size() < props.getPurchasesDefaultPageSize()) {
//            log.warn(
//                "На странице обнаружено меньше карточек, чем указано в конфигурации отображаемого их количества: " +
//                purchaseUrlElements.size() +
//                "/" +
//                props.getPurchasesDefaultPageSize()
//            );
//        }
//
//        for (SelenideElement cardUrlElement : purchaseUrlElements) {
//            if (cardUrlElement.attr("class") == null) {
//                continue; // какой-то баг?
//            }
//            if (cardUrlElement.attr("class").contains("lots-wrap")) {
//                log.warn("У карточки обнаружено дополнительное поле 'Лоты закупки', которое мы ещё никак не обрабатываем!");
//            }
//            cards.add(cardUrlElement); // addAll()
//        }
//
//        if (cards.size() == 0) {
//            log.warn("Не было найдено ни одной карточки на странице либо доступные страницы закончились.");
//            return false;
//        }
//
//        for (SelenideElement card : cards) {
//            if (card.attr("class").contains("lots-wrap-row")) {
//                continue; // пробилось многострочное дополнительное поле 'Лоты закупки'. Это не карточка, а часть прошлой карточки. Пропускаем.
//            }
//
//            ElementsCollection hrefs = card.$$x("*//a").filter(Condition.partialText("№"));
//            if (hrefs.size() == 0) {
//                throw new UnknownParseException("Не удалось найти номер карточки в тексте '" + card.text() + "'");
//            }
//
//            String cardLink = hrefs.get(0).attr("href"); // or card.text().split("\n") = 3 data lines.
//            Objects.requireNonNull(cardLink);
//
//            Optional<PurchasePage> existingPage = purchasePageService.findOneByUrl(cardLink);
//            if (existingPage.isEmpty()) {
//                isNewItemsFound = true;
//                newItemsCount++;
//
//                PurchasePage page = new PurchasePage();
//                //page.setStatus(PREPARING.name()); // маркер
//                try {
//                    page.setParserGuid(guid);
//                    page.setPublicationOwner("https://zakupki.gov.ru/");
//                    page.setUrl(cardLink);
//                    page.setNumber(hrefs.get(0).text());
//                    page.setStage(card.parent().$x("./div/div[2]/div[2]").text());
//                    if (card.parent().$$x("./div//div").filter(Condition.text("Объект закупки")).size() != 0) {
//                        page.setTitle(
//                            card.parent().$$x("./div//div").filter(Condition.text("Объект закупки")).get(0).text().split("\n")[1]
//                        ); // объект закупки
//                    } else {
//                        page.setTitle("Объект закупки не указан");
//                    }
//
//                    ElementsCollection priceBlock = card.parent().parent().$$x("./div//div").filter(Condition.text("Начальная цена"));
//                    if (priceBlock.size() != 0) { // || !card.parent().parent().$x("./div//div[@Class='price-block']").text().isBlank()
//                        page.setPrice(priceBlock.get(0).text().split("\n")[1].replaceAll("[₽¥$€৳]", "").replaceAll("\\s", "").trim());
//                    }
//
//                    String lawType = card.$(".row.registry-entry__header-top").$x("./div/div").text();
//                    if (lawType.startsWith("223-ФЗ")) {
//                        page.setLaw("223-ФЗ");
//                        page.setType(lawType.replace("223-ФЗ", "").trim());
//                    } else if (lawType.startsWith("44-ФЗ")) {
//                        page.setLaw("44-ФЗ");
//                        page.setType(lawType.replace("44-ФЗ", "").trim());
//                    } else if (lawType.startsWith("ПП РФ 615")) {
//                        page.setLaw("ПП РФ 615");
//                        page.setType(lawType.replace("ПП РФ 615", "").trim());
//                    } else if (lawType.startsWith("94-ФЗ")) {
//                        page.setLaw("94-ФЗ");
//                        page.setType(lawType.replace("94-ФЗ", "").trim());
//                    } else {
//                        log.error("Fix this!");
//                    }
//
//                    setDates(page, card.$$x("../../..//div[@Class='data-block__title']"));
//                } catch (Throwable t) {
//                    log.error("Возникла проблема при обработке очередной карточки: " + t);
//                }
//
//                ElementsCollection customers = card.parent().$$(".registry-entry__body-href a").filter(Condition.visible);
//                if (customers.size() == 1) {
//                    page.setCustomerUrl(customers.get(0).attr("href"));
//                } else {
//                    log.error("CustomerUrl не найдена в блоке " + card.parent().innerHtml());
//                }
//
//                page.setObtainedDate(LocalDateTime.now(ZoneId.of("Europe/Moscow")).toInstant(ZoneOffset.UTC));
//                page.setStatus(NOT_PARSED.name()); // маркер
//
//                log.debug("Сохранение в коллекцию найденных очередной карточки закупки. Время: " + page.getObtainedDate());
//                foundSetToSave.add(page);
//            } else {
//                /* Уже есть такая existingPage в БД. Возможно, стоит её обновить? Мог измениться статус закупки и т.п.
//                 *  ТОЛЬКО ЕСЛИ ОБНАРУЖЕННАЯ КОПИЯ - НОВЕЕ ПО ПОЛЮ 'Обновлено' (updated_date) !!!
//                 */
//                try {
//                    PurchasePage foundPage = existingPage.get(); // обнаруженная "копия".
//                    Optional<PurchasePage> existsPageOpt = purchasePageService.findOneByUrl(foundPage.getUrl()); // имеющаяся в БД версия
//                    if (existsPageOpt.isPresent()) {
//                        PurchasePage existsPage = existsPageOpt.get();
//                        if (existsPage.getUpdatedDate() == null || existsPage.getUpdatedDate().isBefore(foundPage.getUpdatedDate())) {
//                            log.info(
//                                "Обнаружено обновление существующей карточки " +
//                                existsPage.getTitle() +
//                                ". Её дата обновления: " +
//                                existsPage.getUpdatedDate() +
//                                ", найдено обновление: " +
//                                foundPage.getUpdatedDate() +
//                                ". Статус карточки будет сброшен для репарсинга."
//                            );
//                            if (!existsPage.getStatus().equals(NOT_PARSED.name())) {
//                                existsPage.setStatus(NOT_PARSED.name()); // маркер
//                                existsPage.setObtainedDate(LocalDateTime.now(ZoneId.of("Europe/Moscow")).toInstant(ZoneOffset.UTC));
//                                purchasePageService.save(existsPage);
//                                newItemsCount++; // так же зачитывается как найденная закупка.
//                            }
//                            // TODO: достаточно ли менять маркер на NOT_PARSED для перепарсинга карточки?
//                            //  или лучше вообще удалять карточку, заливая новую версию? Ничего не будет ломаться при этом? Уточнить.
//                            //  Если же дубликатов вообще не бывает - требуется понять откуда тогда пробелы в поиске закупок, когда находится всего 4 тысячи карточек,
//                            //  при том, что на сайте-источнике написано "более 15 000 результатов".
//                        }
//                    }
//                } catch (Throwable t) {
//                    throw new UnknownParseException(
//                        "Была найдена обновлённая карточка, но в процессе обновления произошла ошибка: " + t.getMessage(),
//                        t
//                    );
//                }
//            }
//
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                // ignore sleep
//            }
//        }
//
//        // сохранение найденных карточек:
//        if (foundSetToSave.size() > 0) {
//            purchasePageService.saveAll(foundSetToSave);
//        }
//
//        // указываем количество найденных карточек:
//        if (isNewItemsFound) {
//            parserTaskDTO.addParsedItemsCount(newItemsCount);
//        }
//        parserTaskService.save(parserTaskDTO);
//
//        log.debug("Новый лист закупок прочитан. Найдено новых карточек: {}", newItemsCount);
//        return isNewItemsFound;
//    }

//    private void setDates(PurchasePage page, ElementsCollection datesBlocks) {
//        if (datesBlocks.filter(Condition.text("Размещено")).size() == 1) {
//            page.setPublicationDate(
//                UniversalDateParser.parseDateTimeGMT(datesBlocks.filter(Condition.text("Размещено")).get(0).sibling(0).text())
//            );
//        }
//
//        if (datesBlocks.filter(Condition.text("Обновлено")).size() == 1) {
//            page.setUpdatedDate(
//                UniversalDateParser.parseDateTimeGMT(datesBlocks.filter(Condition.text("Обновлено")).get(0).sibling(0).text())
//            );
//        }
//
//        if (datesBlocks.filter(Condition.text("Окончание подачи заявок")).size() == 1) {
//            page.setDeadlineDate(
//                UniversalDateParser.parseDateTimeGMT(datesBlocks.filter(Condition.text("Окончание подачи заявок")).get(0).sibling(0).text())
//            );
//        }
//    }

//    private void doReparseAll(StateContext<ParserStates, ParserEvents> context) {
//        Selenide.sleep(props.getEachStateSleepMs());
//        log.debug("Send to machine the 'REPARSE_ALL' command...");
//
//        PurchaseList_SetupAndLoadAction.resetPage();
//        ParserTaskDTO parserTaskDTO = parserTaskService.findOneByGuid(guid).orElseThrow();
//        parserTaskDTO.setParsedPagesCount(0L);
//        parserTaskService.save(parserTaskDTO);
//
//        context.getStateMachine().sendEvent(ParserEvents.REPARSE_ALL);
//    }

//    private boolean isDepthAllowToParseNext() {
//        return PurchaseList_SetupAndLoadAction.getPage() < props.getPurchasesItemsDepth();
//    }
}
