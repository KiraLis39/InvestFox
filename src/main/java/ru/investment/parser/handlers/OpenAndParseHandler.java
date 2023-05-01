package ru.investment.parser.handlers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class OpenAndParseHandler {

    private final ApplicationContext appCtx;

    public void toParse() {

    }

//    public void toParse() {
//        final ParserThread parserThread = ((ParserThread) Thread.currentThread());
//        final PurchasePage purchasePage = (PurchasePage) parserThread.get(ParserThread.ENVIRONMENTS.PURCHASE_PAGE);
//        Purchase purchase = purchaseService.findOneByUrl(purchasePage.getUrl().trim()).orElse(new Purchase());
//        if (purchase.getId() == null) {
//            purchase.setUrl(purchasePage.getUrl().trim());
//        }
//
//        purchase.setTitle(purchasePage.getTitle());
//        if (purchasePage.getNumber() != null) {
//            purchase.setNumber(purchasePage.getNumber().replace("№ ", "").trim());
//        }
//        purchase.setLaw(purchasePage.getLaw());
//        purchase.setType(purchasePage.getType());
//        purchase.setPublicationDate(purchasePage.getPublicationDate());
//        purchase.setUpdatedDate(purchasePage.getUpdatedDate());
//        purchase.setDeadlineDate(purchasePage.getDeadlineDate());
//        purchase.setStage(purchasePage.getStage());
//        if (purchasePage.getPrice() != null) {
//            purchase.setPrice(UniversalNumberParser.parseDouble(purchasePage.getPrice()));
//        }
//
//        try {
//            // меняем статус позиции в админке:
//            parserStateService.updateTaskState(ParserEvents.ON_PARSING_DATA, parserThread);
//
//            // открываем окно браузера:
//            open();
//
//            // открываем в нём страницу и парсим:
//            openPage(purchase.getUrl());
//
//            // меняем статус позиции в админке:
//            purchasePage.setStatus(PurchaseStates.PARSING);
//
//            // парсим карточку закупки
//            appCtx.getBean(PurchaseItemParser.class).parseItem(purchase);
//        } catch (ParsingException pe) {
//            log.error("Ошибка шаблона:", pe);
//            parserStateService.saveErrorInfo(pe, "Ошибка шаблона: " + pe.getMessage());
//            markThisError(parserThread);
//        } catch (Throwable t) {
//            PurchasePage page = (PurchasePage) parserThread.get(ParserThread.ENVIRONMENTS.PURCHASE_PAGE);
//            if (
//                t.getCause() != null &&
//                (t.getCause() instanceof InterruptedException || t.getCause().getCause() instanceof InterruptedException)
//            ) {
//                log.error("Работа потока была прервана: " + t.getMessage());
//                page.setStatus(PurchaseStates.NOT_PARSED);
//                ((ParserThread) Thread.currentThread()).store(
//                        ParserThread.ENVIRONMENTS.PURCHASE_PAGE,
//                        purchasePageService.saveAndFlush(page)
//                    );
//                parserStateService.saveErrorInfo(t, "Работа потока была прервана");
//                parserThread.store(ParserThread.ENVIRONMENTS.PURCHASE_PAGE, purchasePageService.saveAndFlush(page));
//                ((ParserThread) Thread.currentThread()).getStateMachine().sendEvent(ParserEvents.ON_ACCOMPLISHED);
//            } else {
//                log.error("Непредвиденная ошибка парсера или БД: " + t.getMessage(), t);
//                page.setStatus(PurchaseStates.ERROR_OCCURRED);
//                ((ParserThread) Thread.currentThread()).store(
//                        ParserThread.ENVIRONMENTS.PURCHASE_PAGE,
//                        purchasePageService.saveAndFlush(page)
//                    );
//                parserStateService.saveErrorInfo(t, "Непредвиденная ошибка парсера: " + t.getMessage());
//                parserThread.store(ParserThread.ENVIRONMENTS.PURCHASE_PAGE, purchasePageService.saveAndFlush(page));
//                ((ParserThread) Thread.currentThread()).getStateMachine().sendEvent(ParserEvents.ON_ERROR_OCCURRED);
//            }
//        }
//    }
//
//    private void markThisError(ParserThread parserThread) {
//        PurchasePage page =
//            ((PurchasePage) parserThread.get(ParserThread.ENVIRONMENTS.PURCHASE_PAGE)).status(PurchaseStates.ERROR_OCCURRED.name());
//        parserThread.store(ParserThread.ENVIRONMENTS.PURCHASE_PAGE, purchasePageService.saveAndFlush(page));
//        parserThread.getStateMachine().sendEvent(ParserEvents.ON_ERROR_OCCURRED);
//    }
//
//    private void openPage(String url) throws NoDataAvailableException {
//        log.info("Поток " + ((ParserThread) Thread.currentThread()).getGuid() + " начал парсинг закупки " + url);
//        open(url);
//        $x("/html/body").shouldBe(Condition.visible);
//
//        // поиск неисправностей:
//        if (BrowserUtils.isPageNotAvailable()) {
//            throw new NoDataAvailableException("Страница временно недоступна: " + url);
//        }
//        if (BrowserUtils.isPageNotFound()) {
//            throw new NoDataAvailableException("Страница не найдена: " + url);
//        }
//        if (BrowserUtils.isTechnicalWorks()) {
//            ((ParserThread) Thread.currentThread()).getStateMachine().sendEvent(ParserEvents.ON_TECHNICAL_WORK);
//            parserStateService.updateTaskState("Технические работы", ((ParserThread) Thread.currentThread()));
//        }
//    }
}
